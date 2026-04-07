package com.piedrazul.appointments.medico.entity;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import com.piedrazul.appointments.shared.enums.Especialidad;
import com.piedrazul.appointments.shared.enums.RolUsuario;

import com.piedrazul.appointments.shared.entity.Usuario;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "medicos_terapistas")
@DiscriminatorValue("MEDICO_TERAPISTA")
public class MedicoTerapista extends Usuario {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Especialidad especialidad;

    @Column(nullable = false)
    private Integer intervaloCitas;

    @Column(nullable = false)
    private LocalTime horaInicio;

    @Column(nullable = false)
    private LocalTime horaFin;

    @ElementCollection
    @CollectionTable(name = "medico_dias_atencion", joinColumns = @JoinColumn(name = "medico_id"))
    @Column(name = "dia")
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> diasAtencion = new HashSet<>();

    public MedicoTerapista(String username,
            String password,
            String nombres,
            String apellidos,
            boolean activo,
            Especialidad especialidad,
            Integer intervaloCitas,
            LocalTime horaInicio,
            LocalTime horaFin) {
        super(username, password, nombres, apellidos, activo, RolUsuario.MEDICO_TERAPISTA);
        this.especialidad = especialidad;
        this.intervaloCitas = intervaloCitas;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }
}
