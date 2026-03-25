package com.piedrazul.appointments.controllers;

import com.piedrazul.appointments.DTOs.request.ConfiguracionMedicoRequest;
import com.piedrazul.appointments.DTOs.request.VentanaSemanasRequest;
import com.piedrazul.appointments.DTOs.response.MedicoTerapistaResponse;
import com.piedrazul.appointments.entities.ConfiguracionSistema;
import com.piedrazul.appointments.mappers.MedicoTerapistaMapper;
import com.piedrazul.appointments.services.interfaces.IConfiguracionSistemaService;
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