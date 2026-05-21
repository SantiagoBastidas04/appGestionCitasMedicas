package com.piedrazul.appointments.auth.controller;

import com.piedrazul.appointments.auth.dto.RegistroPacienteRequest;
import com.piedrazul.appointments.paciente.dto.PacienteResponse;
import com.piedrazul.appointments.paciente.entity.Paciente;
import com.piedrazul.appointments.paciente.mapper.PacienteMapper;
import com.piedrazul.appointments.paciente.service.IPacienteService;
import com.piedrazul.appointments.shared.exception.CorreoDuplicadoException;
import com.piedrazul.appointments.shared.exception.DocumentoDuplicadoException;
import com.piedrazul.appointments.shared.exception.RegistroException;
import com.piedrazul.appointments.shared.exception.UsuarioDuplicadoException;
import com.piedrazul.appointments.shared.security.KeycloakAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IPacienteService pacienteService;
    private final PacienteMapper pacienteMapper;
    private final PasswordEncoder passwordEncoder;
    private final KeycloakAdminService keycloakAdminService;

    @PostMapping("/registro")
    public ResponseEntity<PacienteResponse> registro(
            @Valid @RequestBody RegistroPacienteRequest request) {
        String username = request.getUsername().trim().toLowerCase();
        try {
            keycloakAdminService.crearUsuario(
                    username,
                    request.getPassword(),
                    request.getNombres(),
                    request.getApellidos(),
                    "PACIENTE"
            );
        } catch (KeycloakAdminService.KeycloakUserCreationException e) {
            throw new UsuarioDuplicadoException(username);
        }

        try {
            Paciente paciente = new Paciente(
                    username,
                    passwordEncoder.encode(request.getPassword()),
                    request.getNombres(), request.getApellidos(),
                    request.getNumeroDocumento(), request.getCelular(),
                    true, request.getGenero(),
                    request.getFechaNacimiento(), request.getCorreoElectronico()
            );
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(pacienteMapper.toResponse(pacienteService.guardarPaciente(paciente)));

        } catch (DocumentoDuplicadoException e) {
            keycloakAdminService.eliminarUsuario(username);
            throw e;
        } catch (CorreoDuplicadoException e) {
            keycloakAdminService.eliminarUsuario(username);
            throw e;
        } catch (Exception e) {
            keycloakAdminService.eliminarUsuario(username);
            throw new RegistroException("No se pudo completar el registro. Intente de nuevo.");
        }
    }
}
