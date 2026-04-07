package com.piedrazul.appointments.configuracion.controller;

import com.piedrazul.appointments.medico.dto.ConfiguracionMedicoRequest;
import com.piedrazul.appointments.cita.dto.VentanaSemanasRequest;
import com.piedrazul.appointments.medico.dto.MedicoTerapistaResponse;
import com.piedrazul.appointments.configuracion.entity.ConfiguracionSistema;
import com.piedrazul.appointments.medico.mapper.MedicoTerapistaMapper;
import com.piedrazul.appointments.configuracion.service.IConfiguracionSistemaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/configuracion")
@RequiredArgsConstructor
public class ConfiguracionController {

    private final IConfiguracionSistemaService configuracionService;
    private final MedicoTerapistaMapper medicoMapper;

    @GetMapping
    public ResponseEntity<ConfiguracionSistema> obtenerConfiguracion() {
        return ResponseEntity.ok(configuracionService.obtenerConfiguracion());
    }

    @PutMapping("/ventana")
    public ResponseEntity<ConfiguracionSistema> actualizarVentana(
            @Valid @RequestBody VentanaSemanasRequest request) {
        return ResponseEntity.ok(
                configuracionService.actualizarVentanaSemanas(request.getVentanaSemanas())
        );
    }

    @PutMapping("/medico/{medicoId}")
    public ResponseEntity<MedicoTerapistaResponse> configurarMedico(
            @PathVariable Long medicoId,
            @Valid @RequestBody ConfiguracionMedicoRequest request) {
        return ResponseEntity.ok(
                medicoMapper.toResponse(
                        configuracionService.configurarMedico(medicoId, request)
                )
        );
    }
}