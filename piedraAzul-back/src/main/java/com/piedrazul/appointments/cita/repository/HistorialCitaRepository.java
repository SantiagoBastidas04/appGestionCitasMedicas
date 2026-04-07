package com.piedrazul.appointments.cita.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.piedrazul.appointments.cita.entity.Cita;
import com.piedrazul.appointments.cita.entity.HistorialCita;

@Repository
public interface HistorialCitaRepository extends JpaRepository<HistorialCita, Long> {
    // Ver el historial de cambios de una cita específica
    List<HistorialCita> findByCitaOrderByFechaCambioDesc(Cita cita);
}
