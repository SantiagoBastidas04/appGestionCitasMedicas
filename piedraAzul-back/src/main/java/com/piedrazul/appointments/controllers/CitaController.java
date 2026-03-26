package com.piedrazul.appointments.controllers;

import com.piedrazul.appointments.DTOs.request.CitaRequest;
import com.piedrazul.appointments.DTOs.request.ReAgendarCitaRequest;
import com.piedrazul.appointments.DTOs.response.CitaResponse;
import com.piedrazul.appointments.entities.*;
import com.piedrazul.appointments.mappers.CitaMapper;
import com.piedrazul.appointments.security.AuthUtils;
import com.piedrazul.appointments.services.interfaces.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {

    private final ICitaService citaService;
    private final IPacienteService pacienteService;
    private final IMedicoTerapistaService medicoService;
    private final IUsuarioService usuarioService;
    private final CitaMapper citaMapper;
    private final AuthUtils authUtils;

    @GetMapping("/medico/{medicoId}/fecha/{fecha}")
    public ResponseEntity<List<CitaResponse>> listarPorMedicoYFecha(
            @PathVariable Long medicoId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        List<CitaResponse> citas = citaService.listarCitasPorMedicoYFecha(medicoId, fecha)
                .stream()
                .map(citaMapper::toResponse)
                .toList();

        return ResponseEntity.ok(citas);
    }
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<CitaResponse>> listarPorPaciente(
            @PathVariable Long pacienteId) {
        List<CitaResponse> citas = citaService.listarCitasPorPaciente(pacienteId)
                .stream()
                .map(citaMapper::toResponse)
                .toList();
        return ResponseEntity.ok(citas);
    }
    @PostMapping
    public ResponseEntity<CitaResponse> crearCita(
            @Valid @RequestBody CitaRequest request) {

        MedicoTerapista medico = medicoService.buscarPorId(request.getMedicoId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Médico no encontrado con id: " + request.getMedicoId()
                ));

        Paciente paciente = pacienteService.buscarPacientePorId(request.getPacienteId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Paciente no encontrado con id: " + request.getPacienteId()
                ));

        // Obtiene el usuario autenticado
        Usuario registradoPor = authUtils.getUsuarioAutenticado();

        Cita cita = citaService.crearCita(
                medico,
                paciente,
                request.getFecha(),
                request.getHora(),
                registradoPor,
                request.getObservacion()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(citaMapper.toResponse(cita));
    }

    @PutMapping("/{id}/reagendar")
    public ResponseEntity<CitaResponse> reagendarCita(
            @PathVariable Long id,
            @Valid @RequestBody ReAgendarCitaRequest request) {

        //  Obtiene el usuario autenticado
        Usuario modificadoPor = authUtils.getUsuarioAutenticado();

        Cita cita = citaService.reAgendarCita(
                id,
                request.getNuevaFecha(),
                request.getNuevaHora(),
                modificadoPor
        );

        return ResponseEntity.ok(citaMapper.toResponse(cita));
    }


    @GetMapping("/{medicoId}/franjas")
    public ResponseEntity<List<LocalTime>> obtenerFranjasDisponibles(
            @PathVariable Long medicoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(
                citaService.calcularFranjasDisponibles(medicoId, fecha)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CitaResponse> cancelarCita(@PathVariable Long id) {
        return ResponseEntity.ok(
                citaMapper.toResponse(citaService.cancelarCita(id))
        );
    }
}