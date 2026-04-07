package com.piedrazul.appointments.cita.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CitaRequest {

    @NotNull(message = "El id del médico es obligatorio")
    private Long medicoId;

    @NotNull(message = "El id del paciente es obligatorio")
    private Long pacienteId;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    private LocalTime hora;

    private String observacion;
}
