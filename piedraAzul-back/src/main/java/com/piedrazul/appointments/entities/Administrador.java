package com.piedrazul.appointments.entities;

import com.piedrazul.appointments.entities.enums.RolUsuario;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "administradores")
@DiscriminatorValue("ADMINISTRADOR")
public class Administrador extends Usuario {
    public Administrador(String username, String password, String nombres, String apellidos, boolean activo) {
        super(username, password, nombres, apellidos, activo, RolUsuario.ADMINISTRADOR);
    }
}
