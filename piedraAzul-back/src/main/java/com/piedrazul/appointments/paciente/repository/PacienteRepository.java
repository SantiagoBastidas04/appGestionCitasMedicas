package com.piedrazul.appointments.paciente.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.piedrazul.appointments.paciente.entity.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    // Buscar paciente por documento
    Optional<Paciente> findByNumeroDocumento(String numeroDocumento);

    // Verificar si ya existe el documento
    boolean existsByNumeroDocumento(String numeroDocumento);

    // Buscar paciente por nombre de usuario
    Optional<Paciente> findByUsername(String username);
}
