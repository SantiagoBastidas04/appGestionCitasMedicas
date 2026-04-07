package com.piedrazul.appointments.cita.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class VentanaSemanasRequest {

    @NotNull(message = "La ventana de semanas es obligatoria")
    @Min(value = 1, message = "La ventana mínima es 1 semana")
    @Max(value = 10, message = "La ventana máxima es 10 semanas")
    private Integer ventanaSemanas;
}