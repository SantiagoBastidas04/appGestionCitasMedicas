package com.piedrazul.appointments.services;

import com.piedrazul.appointments.entities.Paciente;
import com.piedrazul.appointments.repositories.PacienteRepository;
import com.piedrazul.appointments.services.interfaces.IPacienteService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PacienteService implements IPacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Override
    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }

    @Override
    public Optional<Paciente> buscarPacientePorId(Long id) {
        return pacienteRepository.findById(id);
    }

    @Override
    public Optional<Paciente> buscarPacientePorNumDocumento(String numeroDocumento) {
        return pacienteRepository.findByNumeroDocumento(numeroDocumento);
    }

    @Override
    @Transactional
    public Paciente guardarPaciente(Paciente paciente) {
        if (pacienteRepository.existsByNumeroDocumento(paciente.getNumeroDocumento())) {
            throw new IllegalArgumentException(
                    "Ya existe un paciente con el documento: " + paciente.getNumeroDocumento()
            );
        }
        return pacienteRepository.save(paciente);
    }

    @Override
    @Transactional
    public Paciente actualizarPaciente(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }
}
