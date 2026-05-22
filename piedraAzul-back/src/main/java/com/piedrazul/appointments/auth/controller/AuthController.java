package com.piedrazul.appointments.auth.controller;

import com.piedrazul.appointments.auth.dto.RegistroPacienteRequest;
import com.piedrazul.appointments.auth.service.IAuthService;
import com.piedrazul.appointments.paciente.dto.PacienteResponse;
import com.piedrazul.appointments.paciente.entity.Paciente;
import com.piedrazul.appointments.paciente.mapper.PacienteMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;
    private final PacienteMapper pacienteMapper;
    
   /*Se deja el controlador sin logica y se la asigna al servicio */
   @PostMapping("/registro")
   public ResponseEntity<PacienteResponse> registro
   (@Valid @RequestBody RegistroPacienteRequest registroPacienteRequest)
    { 
        Paciente paciente = authService.registrarPaciente(registroPacienteRequest); 
        return ResponseEntity.ok(pacienteMapper.toResponse(paciente));
   }
   
}
