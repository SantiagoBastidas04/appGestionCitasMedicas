package com.piedrazul.appointments.auth.controller;

import com.piedrazul.appointments.auth.dto.LoginRequest;
import com.piedrazul.appointments.auth.dto.RegistroPacienteRequest;
import com.piedrazul.appointments.auth.dto.LoginResponse;
import com.piedrazul.appointments.paciente.dto.PacienteResponse;
import com.piedrazul.appointments.paciente.entity.Paciente;
import com.piedrazul.appointments.shared.entity.Usuario;
import com.piedrazul.appointments.paciente.mapper.PacienteMapper;
import com.piedrazul.appointments.shared.security.JwtUtil;
import com.piedrazul.appointments.paciente.service.IPacienteService;
import com.piedrazul.appointments.shared.service.IUsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final IPacienteService pacienteService;
    private final IUsuarioService usuarioService;
    private final PacienteMapper pacienteMapper;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String rol = userDetails.getAuthorities().iterator().next()
                .getAuthority().replace("ROLE_", "");

        String token = jwtUtil.generarToken(userDetails.getUsername(), rol);

        // Busca el usuario
        Usuario usuario = usuarioService.buscarPorUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        LoginResponse response = new LoginResponse(
                token,
                userDetails.getUsername(),
                rol,
                usuario.getNombres(),
                usuario.getApellidos()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/registro")
    public ResponseEntity<PacienteResponse> registro(
            @Valid @RequestBody RegistroPacienteRequest request) {

        // Cifra la contraseña antes de guardar
        Paciente paciente = new Paciente(
                request.getUsername(),
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

        Paciente guardado = pacienteService.guardarPaciente(paciente);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pacienteMapper.toResponse(guardado));
    }
}
