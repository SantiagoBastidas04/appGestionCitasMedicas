package com.piedrazul.appointments.cita.dto;

import com.piedrazul.appointments.shared.enums.EstadoCita;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CitaResponse {
    private Long id;
    private Long medicoId;
    private String nombreMedico;
    private Long pacienteId;
    private String nombrePaciente;
    private String numeroDocumentoPaciente;
    private String celularPaciente;
    private LocalDate fecha;
    private LocalTime hora;
    private EstadoCita estado;
    private String observacion;
    private String registradoPor;
}