package com.piedrazul.appointments.auth.service;

import com.piedrazul.appointments.auth.dto.RegistroPacienteRequest;
import com.piedrazul.appointments.paciente.entity.Paciente;
import com.piedrazul.appointments.paciente.service.IPacienteService;
import com.piedrazul.appointments.shared.exception.*;
import com.piedrazul.appointments.shared.security.KeycloakAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final IPacienteService pacienteService;
    private final KeycloakAdminService keycloakAdminService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Paciente registrarPaciente(RegistroPacienteRequest request) {
        String username = request.getUsername().trim().toLowerCase();

        // 1. Crear en Keycloak
        try {
            keycloakAdminService.crearUsuario(
                    username,
                    request.getPassword(),
                    request.getNombres(),
                    request.getApellidos(),
                    "PACIENTE"
            );
        } catch (KeycloakAdminService.KeycloakBadRequestException e) {
            throw new KeycloakValidationException(e.getMessage());
        } catch (KeycloakAdminService.KeycloakConflictException e) {
            throw new UsuarioDuplicadoException(username);
        } catch (KeycloakAdminService.KeycloakServiceException e) {
            throw new RegistroException(e.getMessage());
        }

        // 2. Guardar en H2 con rollback si falla
        try {
            Paciente paciente = new Paciente(
                    username,
                    passwordEncoder.encode(request.getPassword()),
                    request.getNombres(),
                    request.getApellidos(),
                    request.getNumeroDocumento(),
                    request.getCelular(),
                    true,
                    request.getGenero(),
                    request.getFechaNacimiento(),
                    request.getCorreoElectronico()
            );

            return pacienteService.guardarPaciente(paciente);

        } catch (DocumentoDuplicadoException | CorreoDuplicadoException e) {
            keycloakAdminService.eliminarUsuario(username);
            throw e;
        } catch (Exception e) {
            keycloakAdminService.eliminarUsuario(username);
            throw new RegistroException("No se pudo completar el registro. Intente de nuevo.");
        }
    }
}