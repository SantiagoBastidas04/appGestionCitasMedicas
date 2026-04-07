package com.piedrazul.appointments.medico.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.piedrazul.appointments.medico.entity.MedicoTerapista;
import com.piedrazul.appointments.shared.enums.Especialidad;

@Repository
public interface MedicoTerapistaRepository extends JpaRepository<MedicoTerapista, Long> {

    // Listar solo medicos activos
    List<MedicoTerapista> findByActivoTrue();

    // Listar por especialidad
    List<MedicoTerapista> findByEspecialidadAndActivoTrue(Especialidad especialidad);
}
