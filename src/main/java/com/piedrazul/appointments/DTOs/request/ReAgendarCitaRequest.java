package com.piedrazul.appointments.DTOs.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReAgendarCitaRequest {

    @NotNull(message = "La nueva fecha es obligatoria")
    private LocalDate nuevaFecha;

    @NotNull(message = "La nueva hora es obligatoria")
    private LocalTime nuevaHora;

    @NotNull(message = "El id del usuario que reagenda es obligatorio")
    private Long modificadoPorId;
}
