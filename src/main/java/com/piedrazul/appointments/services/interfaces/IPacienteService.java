package com.piedrazul.appointments.services.interfaces;

import com.piedrazul.appointments.entities.Paciente;

import java.util.*;

public interface IPacienteService {
    List<Paciente> listarPacientes();
    Optional<Paciente> buscarPacientePorId(Long id);
    Optional<Paciente> buscarPacientePorNumDocumento(String numeroDocumento);
    Paciente guardarPaciente(Paciente paciente);
    Paciente actualizarPaciente(Paciente paciente);
    Optional<Paciente> buscarPacientePorUsername(String username);
}
