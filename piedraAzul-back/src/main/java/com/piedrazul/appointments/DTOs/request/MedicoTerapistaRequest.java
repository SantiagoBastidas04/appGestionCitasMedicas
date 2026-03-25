package com.piedrazul.appointments.DTOs.request;

import com.piedrazul.appointments.entities.enums.Especialidad;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.*;
import java.util.Set;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicoTerapistaRequest {

    @NotBlank(message = "El username es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    @NotBlank(message = "Los nombres son obligatorios")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    @NotNull(message = "La especialidad es obligatoria")
    private Especialidad especialidad;

    @NotNull(message = "El intervalo de citas es obligatorio")
    @Min(value = 5, message = "El intervalo mínimo es 5 minutos")
    private Integer intervaloCitas;

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;

    @NotEmpty(message = "Debe seleccionar al menos un día de atención")
    private Set<DayOfWeek> diasAtencion;
}
