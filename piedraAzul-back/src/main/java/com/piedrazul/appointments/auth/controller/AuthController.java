package com.piedrazul.appointments.auth.controller;

import com.piedrazul.appointments.auth.dto.RegistroPacienteRequest;
import com.piedrazul.appointments.paciente.dto.PacienteResponse;
import com.piedrazul.appointments.paciente.entity.Paciente;
import com.piedrazul.appointments.paciente.mapper.PacienteMapper;
import com.piedrazul.appointments.paciente.service.IPacienteService;
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
        try {
            keycloakAdminService.crearUsuario(
                    request.getUsername(),
                    request.getPassword(),
                    request.getNombres(),
                    request.getApellidos(),
                    "PACIENTE"
            );
        } catch (KeycloakAdminService.KeycloakUserCreationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .build();
        }

        try {
            Paciente paciente = new Paciente(
                    request.getUsername(),
                    passwordEncoder.encode(request.getPassword()),
                    request.getNombres(), request.getApellidos(),
                    request.getNumeroDocumento(), request.getCelular(),
                    true, request.getGenero(),
                    request.getFechaNacimiento(), request.getCorreoElectronico()
            );
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(pacienteMapper.toResponse(pacienteService.guardarPaciente(paciente)));

        } catch (Exception e) {
            keycloakAdminService.eliminarUsuario(request.getUsername());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
