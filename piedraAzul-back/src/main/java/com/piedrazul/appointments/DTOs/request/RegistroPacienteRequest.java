package com.piedrazul.appointments.DTOs.request;


import com.piedrazul.appointments.entities.enums.Genero;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroPacienteRequest {

    @NotBlank(message = "El username es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @NotBlank(message = "Los nombres son obligatorios")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    @NotBlank(message = "El número de documento es obligatorio")
    private String numeroDocumento;

    @NotBlank(message = "El celular es obligatorio")
    private String celular;

    @NotNull(message = "El género es obligatorio")
    private Genero genero;

    private LocalDate fechaNacimiento;

    @Email(message = "El correo electrónico no es válido")
    private String correoElectronico;
}