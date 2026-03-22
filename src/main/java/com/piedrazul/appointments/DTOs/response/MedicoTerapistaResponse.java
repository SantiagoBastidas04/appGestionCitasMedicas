package com.piedrazul.appointments.DTOs.response;

import com.piedrazul.appointments.entities.enums.Especialidad;
import lombok.*;

import java.time.DayOfWeek;
import java.time.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicoTerapistaResponse {
    private Long id;
    private String nombres;
    private String apellidos;
    private Especialidad especialidad;
    private Integer intervaloCitas;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Set<DayOfWeek> diasAtencion;
    private boolean activo;
}
