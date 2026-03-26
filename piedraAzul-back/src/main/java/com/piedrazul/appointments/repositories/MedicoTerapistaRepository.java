package com.piedrazul.appointments.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.piedrazul.appointments.entities.MedicoTerapista;
import com.piedrazul.appointments.entities.enums.Especialidad;

@Repository
public interface MedicoTerapistaRepository extends JpaRepository<MedicoTerapista, Long> {

    // Listar solo medicos activos
    List<MedicoTerapista> findByActivoTrue();

    // Listar por especialidad
    List<MedicoTerapista> findByEspecialidadAndActivoTrue(Especialidad especialidad);
}
