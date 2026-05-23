package com.piedrazul.appointments.paciente.controller;

import com.piedrazul.appointments.paciente.dto.PacienteRequest;
import com.piedrazul.appointments.paciente.dto.PacienteResponse;
import com.piedrazul.appointments.paciente.entity.Paciente;
import com.piedrazul.appointments.paciente.mapper.PacienteMapper;
import com.piedrazul.appointments.paciente.service.IPacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final IPacienteService pacienteService;
    private final PacienteMapper pacienteMapper;

    @GetMapping
    public ResponseEntity<List<PacienteResponse>> listarPacientes() {
        List<PacienteResponse> pacientes = pacienteService.listarPacientes()
                .stream()
                .map(pacienteMapper::toResponse)
                .toList();
        return ResponseEntity.ok(pacientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponse> buscarPorId(@PathVariable Long id) {
        return pacienteService.buscarPacientePorId(id)
                .map(pacienteMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/documento/{numeroDocumento}")
    public ResponseEntity<PacienteResponse> buscarPorDocumento(
            @PathVariable String numeroDocumento) {
        return pacienteService.buscarPacientePorNumDocumento(numeroDocumento)
                .map(pacienteMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<PacienteResponse> buscarPorUsername(
            @PathVariable String username) {
        return pacienteService.buscarPacientePorUsername(username)
                .map(pacienteMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PacienteResponse> crearPaciente(
            @Valid @RequestBody PacienteRequest request) {
        PacienteResponse response = pacienteMapper.toResponse(
                pacienteService.guardarPaciente(pacienteMapper.toEntity(request)));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponse> actualizarPaciente(
            @PathVariable Long id,
            @Valid @RequestBody PacienteRequest request) {
        return pacienteService.buscarPacientePorId(id)
                .map(pacienteExistente -> {
                    pacienteExistente.setNombres(request.getNombres());
                    pacienteExistente.setApellidos(request.getApellidos());
                    pacienteExistente.setCelular(request.getCelular());
                    pacienteExistente.setGenero(request.getGenero());
                    pacienteExistente.setFechaNacimiento(request.getFechaNacimiento());
                    pacienteExistente.setCorreoElectronico(request.getCorreoElectronico());
                    Paciente actualizado = pacienteService.actualizarPaciente(pacienteExistente);
                    return ResponseEntity.ok(pacienteMapper.toResponse(actualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}