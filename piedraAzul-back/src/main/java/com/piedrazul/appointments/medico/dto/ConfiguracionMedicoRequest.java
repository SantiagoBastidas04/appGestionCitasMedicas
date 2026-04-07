package com.piedrazul.appointments.medico.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionMedicoRequest {

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;

    @NotNull(message = "El intervalo de citas es obligatorio")
    @Min(value = 5, message = "El intervalo mínimo es 5 minutos")
    private Integer intervaloCitas;

    @NotEmpty(message = "Debe seleccionar al menos un día")
    private Set<DayOfWeek> diasAtencion;
}