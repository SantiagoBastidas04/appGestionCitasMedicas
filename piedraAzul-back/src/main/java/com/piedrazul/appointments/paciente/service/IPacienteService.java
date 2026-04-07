package com.piedrazul.appointments.paciente.service;

import com.piedrazul.appointments.paciente.entity.Paciente;

import java.util.*;

public interface IPacienteService {
    List<Paciente> listarPacientes();
    Optional<Paciente> buscarPacientePorId(Long id);
    Optional<Paciente> buscarPacientePorNumDocumento(String numeroDocumento);
    Paciente guardarPaciente(Paciente paciente);
    Paciente actualizarPaciente(Paciente paciente);
    Optional<Paciente> buscarPacientePorUsername(String username);
}
