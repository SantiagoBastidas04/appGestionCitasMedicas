package com.piedrazul.appointments.DTOs.response;


import com.piedrazul.appointments.entities.enums.Genero;
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