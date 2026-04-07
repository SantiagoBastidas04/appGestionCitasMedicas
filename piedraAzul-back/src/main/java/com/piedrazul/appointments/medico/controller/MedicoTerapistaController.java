package com.piedrazul.appointments.medico.controller;

import com.piedrazul.appointments.medico.dto.MedicoTerapistaRequest;
import com.piedrazul.appointments.medico.dto.MedicoTerapistaResponse;
import com.piedrazul.appointments.shared.enums.Especialidad;
import com.piedrazul.appointments.medico.mapper.MedicoTerapistaMapper;
import com.piedrazul.appointments.medico.service.IMedicoTerapistaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
@RequiredArgsConstructor
public class MedicoTerapistaController {

    private final IMedicoTerapistaService medicoService;
    private final MedicoTerapistaMapper medicoMapper;

    @GetMapping
    public ResponseEntity<List<MedicoTerapistaResponse>> listarActivos() {
        List<MedicoTerapistaResponse> medicos = medicoService.listarActivos()
                .stream()
                .map(medicoMapper::toResponse)
                .toList();
        return ResponseEntity.ok(medicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoTerapistaResponse> buscarPorId(@PathVariable Long id) {
        return medicoService.buscarPorId(id)
                .map(medicoMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<List<MedicoTerapistaResponse>> listarPorEspecialidad(
            @PathVariable Especialidad especialidad) {
        List<MedicoTerapistaResponse> medicos = medicoService.listarPorEspecialidad(especialidad)
                .stream()
                .map(medicoMapper::toResponse)
                .toList();
        return ResponseEntity.ok(medicos);
    }


    @PostMapping
    public ResponseEntity<MedicoTerapistaResponse> crearMedico(
            @Valid @RequestBody MedicoTerapistaRequest request) {
        MedicoTerapistaResponse response = medicoMapper.toResponse(
                medicoService.guardar(medicoMapper.toEntity(request))
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PutMapping("/{id}/estado")
    public ResponseEntity<MedicoTerapistaResponse> cambiarEstado(
            @PathVariable Long id,
            @RequestParam boolean activo) {
        return ResponseEntity.ok(
                medicoMapper.toResponse(medicoService.cambiarEstado(id, activo))
        );
    }
}