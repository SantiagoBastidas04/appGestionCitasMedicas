package com.piedrazul.appointments.cita.entity;

import java.time.*;

import com.piedrazul.appointments.medico.entity.MedicoTerapista;
import com.piedrazul.appointments.paciente.entity.Paciente;
import com.piedrazul.appointments.shared.enums.EstadoCita;

import com.piedrazul.appointments.shared.entity.Usuario;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "cita",
        uniqueConstraints = @UniqueConstraint(name = "uk_medico_fecha_hora", columnNames = {"medico_id", "fecha",
                "hora"}))
@NoArgsConstructor
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medico_id", nullable = false)
    private MedicoTerapista medico;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private LocalTime hora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCita estado = EstadoCita.CONFIRMADA;

    @ManyToOne(optional = false)
    @JoinColumn(name = "registrado_por_id", nullable = false)
    private Usuario registradoPor;

    @Column(nullable = false, updatable = false)
    private LocalDateTime creadoEn = LocalDateTime.now();

    private String observacion;

    public Cita(MedicoTerapista medico,
                Paciente paciente,
                LocalDate fecha,
                LocalTime hora,
                Usuario registradoPor,
                String observacion) {
        this.medico = medico;
        this.paciente = paciente;
        this.fecha = fecha;
        this.hora = hora;
        this.registradoPor = registradoPor;
        this.observacion = observacion;
        this.estado = EstadoCita.CONFIRMADA;
    }
}
