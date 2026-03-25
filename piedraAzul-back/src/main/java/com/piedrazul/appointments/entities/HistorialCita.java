package com.piedrazul.appointments.entities;

import java.time.*;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "historial_citas")
public class HistorialCita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cita_id", nullable = false)
    private Cita cita;

    @Column(nullable = false)
    private LocalDate fechaAnterior;

    @Column(nullable = false)
    private LocalTime horaAnterior;

    // Quién hizo el cambio
    @ManyToOne(optional = false)
    @JoinColumn(name = "modificado_por_id", nullable = false)
    private Usuario modificadoPor;

    // Cuándo se hizo el cambio
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCambio = LocalDateTime.now();

    public HistorialCita(Cita cita,
            LocalDate fechaAnterior,
            LocalTime horaAnterior,
            Usuario modificadoPor) {
        this.cita = cita;
        this.fechaAnterior = fechaAnterior;
        this.horaAnterior = horaAnterior;
        this.modificadoPor = modificadoPor;
    }

}
