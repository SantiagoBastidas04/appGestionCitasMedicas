package com.piedrazul.appointments.shared.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Servicio que interactúa con la Admin REST API de Keycloak.
 * <p>
 * Úsalo desde AuthController (o quien maneje el registro de usuarios)
 * para que cada nuevo usuario quede creado tanto en H2 como en Keycloak.
 * <p>
 * Propiedades requeridas en application.properties:
 * keycloak.server-url=http://localhost:8180
 * keycloak.realm=piedrazul
 * keycloak.admin-username=admin
 * keycloak.admin-password=admin
 */
@Slf4j
@Service
public class KeycloakAdminService {

    @Value("${keycloak.server-url}")
    private String keycloakUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin-username}")
    private String adminUsername;

    @Value("${keycloak.admin-password}")
    private String adminPassword;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Crea un usuario en Keycloak con contraseña y rol asignados.
     *
     * @param username  mismo username que se guardará en H2
     * @param password  contraseña en texto plano (Keycloak la hashea internamente)
     * @param nombres   para el campo firstName de Keycloak
     * @param apellidos para el campo lastName de Keycloak
     * @param rol       nombre exacto del rol en Keycloak (ej. "PACIENTE")
     * @throws KeycloakConflictException   si el username ya existe en Keycloak
     * @throws KeycloakBadRequestException si hay un error de validación
     * @throws KeycloakServiceException    si algo falla en Keycloak
     */
    public void crearUsuario(String username, String password,
                             String nombres, String apellidos, String rol) {

        String adminToken = obtenerTokenAdmin();

        try {
            String userId = crearUsuarioEnKeycloak(adminToken, username, nombres, apellidos);
            asignarPassword(adminToken, userId, password);
            asignarRol(adminToken, userId, rol);
            log.info("Usuario '{}' creado en Keycloak con rol '{}'", username, rol);

        } catch (HttpClientErrorException.Conflict e) {
            throw new KeycloakConflictException(
                    "El username '" + username + "' ya existe en Keycloak");
        } catch (HttpClientErrorException.BadRequest e) {
            throw new KeycloakBadRequestException(buildBadRequestMessage(e));
        } catch (Exception e) {
            log.error("Error creando usuario en Keycloak: {}", e.getMessage());
            throw new KeycloakServiceException(
                    "Error al registrar usuario en el sistema de autenticación: " + e.getMessage());
        }
    }

    private String buildBadRequestMessage(HttpClientErrorException.BadRequest e) {
        String body = e.getResponseBodyAsString();
        if (body == null || body.isBlank()) {
            return "Error de validación en Keycloak";
        }
        return "Error de validación en Keycloak: " + body;
    }

    /**
     * Elimina un usuario de Keycloak por username.
     * Útil si el guardado en H2 falla después de crearlo en Keycloak (rollback manual).
     */
    public void eliminarUsuario(String username) {
        try {
            String adminToken = obtenerTokenAdmin();
            String userId = buscarIdUsuario(adminToken, username);
            if (userId != null) {
                String url = keycloakUrl + "/admin/realms/" + realm + "/users/" + userId;
                HttpHeaders headers = new HttpHeaders();
                headers.setBearerAuth(adminToken);
                restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);
                log.info("Usuario '{}' eliminado de Keycloak (rollback)", username);
            }
        } catch (Exception e) {
            log.error("No se pudo eliminar usuario '{}' de Keycloak en el rollback: {}",
                    username, e.getMessage());
        }
    }


    @SuppressWarnings("unchecked")
    private String obtenerTokenAdmin() {
        String url = keycloakUrl + "/realms/master/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", "admin-cli");
        body.add("username", adminUsername);
        body.add("password", adminPassword);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                url, new HttpEntity<>(body, headers), Map.class);

        return (String) response.getBody().get("access_token");
    }

    private String crearUsuarioEnKeycloak(String adminToken, String username,
                                          String nombres, String apellidos) {
        String url = keycloakUrl + "/admin/realms/" + realm + "/users";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminToken);

        Map<String, Object> body = Map.of(
                "username", username,
                "firstName", nombres,
                "lastName", apellidos,
                "enabled", true,
                "emailVerified", true,
                "requiredActions", List.of()
        );

        ResponseEntity<Void> response = restTemplate.postForEntity(
                url, new HttpEntity<>(body, headers), Void.class);

        String location = response.getHeaders().getFirst(HttpHeaders.LOCATION);
        if (location == null) {
            throw new KeycloakServiceException("Keycloak no devolvió el ID del usuario creado");
        }
        return location.substring(location.lastIndexOf('/') + 1);
    }

    private void asignarPassword(String adminToken, String userId, String password) {
        String url = keycloakUrl + "/admin/realms/" + realm +
                "/users/" + userId + "/reset-password";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminToken);

        Map<String, Object> body = Map.of(
                "type", "password",
                "value", password,
                "temporary", false
        );

        restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(body, headers), Void.class);
    }

    @SuppressWarnings("unchecked")
    private void asignarRol(String adminToken, String userId, String rolNombre) {
        String urlRol = keycloakUrl + "/admin/realms/" + realm + "/roles/" + rolNombre;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);

        ResponseEntity<Map> rolResponse = restTemplate.exchange(
                urlRol, HttpMethod.GET, new HttpEntity<>(headers), Map.class);

        String urlAsignar = keycloakUrl + "/admin/realms/" + realm +
                "/users/" + userId + "/role-mappings/realm";

        HttpHeaders headersPost = new HttpHeaders();
        headersPost.setContentType(MediaType.APPLICATION_JSON);
        headersPost.setBearerAuth(adminToken);

        restTemplate.exchange(
                urlAsignar, HttpMethod.POST,
                new HttpEntity<>(List.of(rolResponse.getBody()), headersPost), Void.class);
    }

    @SuppressWarnings("unchecked")
    private String buscarIdUsuario(String adminToken, String username) {
        String url = keycloakUrl + "/admin/realms/" + realm + "/users?username=" + username;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);

        ResponseEntity<List> response = restTemplate.exchange(
                url, HttpMethod.GET, new HttpEntity<>(headers), List.class);

        List<Map<String, Object>> users = response.getBody();
        if (users == null || users.isEmpty()) return null;
        return (String) users.get(0).get("id");
    }


    public static class KeycloakConflictException extends RuntimeException {
        public KeycloakConflictException(String message) {
            super(message);
        }
    }

    public static class KeycloakBadRequestException extends RuntimeException {
        public KeycloakBadRequestException(String message) {
            super(message);
        }
    }

    public static class KeycloakServiceException extends RuntimeException {
        public KeycloakServiceException(String message) {
            super(message);
        }
    }
}