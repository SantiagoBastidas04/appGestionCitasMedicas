package com.piedrazul.appointments.services;

import com.piedrazul.appointments.entities.MedicoTerapista;
import com.piedrazul.appointments.entities.enums.Especialidad;
import com.piedrazul.appointments.repositories.MedicoTerapistaRepository;
import com.piedrazul.appointments.services.interfaces.IMedicoTerapistaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MedicoTerapistaService implements IMedicoTerapistaService {

    @Autowired
    private MedicoTerapistaRepository medicoTerapistaRepository;

    @Override
    public List<MedicoTerapista> listarActivos() {
        return medicoTerapistaRepository.findByActivoTrue();
    }

    @Override
    public List<MedicoTerapista> listarPorEspecialidad(Especialidad especialidad) {
        return medicoTerapistaRepository.findByEspecialidadAndActivoTrue(especialidad);
    }

    @Override
    public Optional<MedicoTerapista> buscarPorId(Long id) {
        return medicoTerapistaRepository.findById(id);
    }

    @Override
    @Transactional
    public MedicoTerapista guardar(MedicoTerapista medicoTerapista) {
        return medicoTerapistaRepository.save(medicoTerapista);
    }

    @Override
    @Transactional
    public MedicoTerapista cambiarEstado(Long id, boolean activo) {
        MedicoTerapista medico = medicoTerapistaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Médico no encontrado con id: " + id
                ));
        medico.setActivo(activo);
        return medicoTerapistaRepository.save(medico);
    }
}
