package com.piedrazul.appointments.paciente.dto;

import com.piedrazul.appointments.shared.enums.Genero;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PacienteRequest {
    @NotBlank(message = "El número de documento es obligatorio")
    private String numeroDocumento;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombres;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellidos;

    @NotBlank(message = "El celular es obligatorio")
    private String celular;

    @NotNull(message = "El género es obligatorio")
    private Genero genero;

    // Opcionales según RF2
    private LocalDate fechaNacimiento;
    private String correoElectronico;
}
