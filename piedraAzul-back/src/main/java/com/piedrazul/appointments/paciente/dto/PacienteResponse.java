package com.piedrazul.appointments.paciente.dto;


import com.piedrazul.appointments.shared.enums.Genero;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PacienteResponse {

    private Long id;
    private String nombres;
    private String apellidos;
    private String numeroDocumento;
    private String celular;
    private Genero genero;
    private LocalDate fechaNacimiento;
    private String correoElectronico;

}