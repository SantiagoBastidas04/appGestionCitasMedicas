package com.piedrazul.appointments.cita.repository;

import com.piedrazul.appointments.cita.entity.Cita;
import com.piedrazul.appointments.medico.entity.MedicoTerapista;
import com.piedrazul.appointments.shared.enums.EstadoCita;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    //  Listar citas de un médico en una fecha determinada
    List<Cita> findByMedicoAndFechaAndEstadoNot(MedicoTerapista medico, LocalDate fecha, EstadoCita estadoExcluido);
    List<Cita> findByPacienteId(Long pacienteId);
    //  Verificar si ya existe una cita en esa hora
    boolean existsByMedicoAndFechaAndHoraAndEstadoNot(MedicoTerapista medico, LocalDate fecha, LocalTime hora, EstadoCita estadoExcluido);

    // Listar las horas ya ocupadas de un médico en una fecha
    @Query("SELECT c.hora FROM Cita c WHERE c.medico = :medico " +
            "AND c.fecha = :fecha AND c.estado != :estado")
    List<LocalTime> findHorasOcupadasByMedicoAndFecha(
            @Param("medico") MedicoTerapista medico,
            @Param("fecha") LocalDate fecha,
            @Param("estado") EstadoCita estado);

}
