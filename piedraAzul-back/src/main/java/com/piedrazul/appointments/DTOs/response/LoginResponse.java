package com.piedrazul.appointments.DTOs.response;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String username;
    private String rol;
    private String nombres;
    private String apellidos;
}