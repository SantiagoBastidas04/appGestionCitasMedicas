package com.piedrazul.appointments.shared.config;

import com.piedrazul.appointments.shared.entity.Usuario;
import com.piedrazul.appointments.shared.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Profile("migrate")
@Order(2)
@RequiredArgsConstructor
public class KeycloakMigrationRunner implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;

    private static final String KEYCLOAK_URL = "http://localhost:8180";
    private static final String REALM = "piedrazul";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private static final String DEFAULT_PASSWORD = "piedrazul123";

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void run(String... args) {
        log.info("=== MIGRACIÓN DE USUARIOS H2 → KEYCLOAK ===");

        String adminToken = obtenerTokenAdmin();
        if (adminToken == null) {
            log.error("No se pudo obtener token de administrador de Keycloak. " +
                    "Verifica que Keycloak esté corriendo en {}", KEYCLOAK_URL);
            return;
        }

        List<Usuario> usuarios = usuarioRepository.findAll();
        int creados = 0, omitidos = 0, errores = 0;

        for (Usuario usuario : usuarios) {
            if (usuario.getUsername() == null || usuario.getUsername().isBlank()) {
                log.warn("Usuario ID={} ({} {}) no tiene username — omitido",
                        usuario.getId(), usuario.getNombres(), usuario.getApellidos());
                omitidos++;
                continue;
            }
            boolean creado = crearUsuarioEnKeycloak(adminToken, usuario);
            if (creado) creados++;
            else errores++;
        }

        log.info("=== MIGRACIÓN COMPLETADA: {} creados, {} omitidos, {} errores ===",
                creados, omitidos, errores);
        log.info("Contraseña asignada a todos: '{}'", DEFAULT_PASSWORD);
    }

    @SuppressWarnings("unchecked")
    private String obtenerTokenAdmin() {
        try {
            String url = KEYCLOAK_URL + "/realms/master/protocol/openid-connect/token";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "password");
            body.add("client_id", "admin-cli");
            body.add("username", ADMIN_USERNAME);
            body.add("password", ADMIN_PASSWORD);
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    url, new HttpEntity<>(body, headers), Map.class);
            return (String) response.getBody().get("access_token");
        } catch (Exception e) {
            log.error("Error obteniendo token admin: {}", e.getMessage());
            return null;
        }
    }

    private boolean crearUsuarioEnKeycloak(String adminToken, Usuario usuario) {
        String rolKeycloak = usuario.getRol().name();
        try {
            String userId = crearUsuario(adminToken, usuario);
            if (userId == null) return false;
            asignarPassword(adminToken, userId, DEFAULT_PASSWORD);
            asignarRol(adminToken, userId, rolKeycloak);
            log.info("✓ username='{}' | nombre='{} {}' | rol='{}'",
                    usuario.getUsername(),
                    usuario.getNombres(), usuario.getApellidos(),
                    rolKeycloak);
            return true;
        } catch (HttpClientErrorException.Conflict e) {
            log.warn("⚠ Usuario '{}' ya existe en Keycloak — omitido", usuario.getUsername());
            return true;
        } catch (Exception e) {
            log.error("✗ Error creando usuario '{}': {}", usuario.getUsername(), e.getMessage());
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private String crearUsuario(String adminToken, Usuario usuario) {
        String url = KEYCLOAK_URL + "/admin/realms/" + REALM + "/users";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminToken);
        Map<String, Object> body = Map.of(
                "username", usuario.getUsername(),
                "firstName", usuario.getNombres(),
                "lastName", usuario.getApellidos(),
                "enabled", usuario.isActivo(),
                "requiredActions", List.of()
        );
        ResponseEntity<Void> response = restTemplate.postForEntity(
                url, new HttpEntity<>(body, headers), Void.class);
        String location = response.getHeaders().getFirst(HttpHeaders.LOCATION);
        if (location == null) return null;
        return location.substring(location.lastIndexOf('/') + 1);
    }

    private void asignarPassword(String adminToken, String userId, String password) {
        String url = KEYCLOAK_URL + "/admin/realms/" + REALM +
                "/users/" + userId + "/reset-password";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminToken);
        Map<String, Object> body = Map.of(
                "type", "password", "value", password, "temporary", false);
        restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(body, headers), Void.class);
    }

    @SuppressWarnings("unchecked")
    private void asignarRol(String adminToken, String userId, String rolNombre) {
        String urlRol = KEYCLOAK_URL + "/admin/realms/" + REALM + "/roles/" + rolNombre;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);
        ResponseEntity<Map> rolResponse = restTemplate.exchange(
                urlRol, HttpMethod.GET, new HttpEntity<>(headers), Map.class);

        String urlAsignar = KEYCLOAK_URL + "/admin/realms/" + REALM +
                "/users/" + userId + "/role-mappings/realm";
        HttpHeaders headersPost = new HttpHeaders();
        headersPost.setContentType(MediaType.APPLICATION_JSON);
        headersPost.setBearerAuth(adminToken);
        restTemplate.exchange(urlAsignar, HttpMethod.POST,
                new HttpEntity<>(List.of(rolResponse.getBody()), headersPost), Void.class);
    }
}