package com.piedrazul.appointments.auth.dto;


import com.piedrazul.appointments.shared.enums.Genero;
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

    @Pattern(
            regexp = "^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ ]+$",
            message = "Los Nombres solo pueden contener letras y espacios"
    )
    @NotBlank(message = "Los nombres son obligatorios")
    private String nombres;
    @Pattern(
            regexp = "^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ ]+$",
            message = "Los Apellidos solo pueden contener letras y espacios"
    )
    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = ("El numero de documento debera tener obligatoriamente 10 digitos")
    )
    @NotBlank(message = "El número de documento es obligatorio")
    private String numeroDocumento;
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "El celular debe contener exactamente 10 dígitos numéricos"
    )
    @NotBlank(message = "El celular es obligatorio")
    private String celular;

    @NotNull(message = "El género es obligatorio")
    private Genero genero;
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    private LocalDate fechaNacimiento;

    @Email(message = "El correo electrónico no es válido")
    private String correoElectronico;
}