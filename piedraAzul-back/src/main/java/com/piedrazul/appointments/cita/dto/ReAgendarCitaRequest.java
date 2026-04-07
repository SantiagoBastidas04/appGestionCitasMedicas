package com.piedrazul.appointments.cita.dto;

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

}
