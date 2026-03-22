package com.piedrazul.appointments.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "configuracion_sistema")
@Getter @Setter
@NoArgsConstructor
public class ConfiguracionSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cuantas semanas hacia adelante se pueden agendar citas
    @Column(nullable = false)
    private Integer ventanaSemanas;

    public ConfiguracionSistema(Integer ventanaSemanas) {
        this.ventanaSemanas = ventanaSemanas;
    }
}