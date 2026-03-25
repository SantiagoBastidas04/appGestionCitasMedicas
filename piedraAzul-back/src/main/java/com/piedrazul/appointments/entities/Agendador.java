package com.piedrazul.appointments.entities;

import com.piedrazul.appointments.entities.enums.RolUsuario;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "agendadores")
@DiscriminatorValue("AGENDADOR")
public class Agendador extends Usuario {
    public Agendador(String username, String password, String nombres, String apellidos, boolean activo) {
        super(username, password, nombres, apellidos, activo, RolUsuario.AGENDADOR);
    }
}
