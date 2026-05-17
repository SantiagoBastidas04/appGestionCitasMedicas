package com.piedrazul.appointments.medico.service;

import com.piedrazul.appointments.medico.dto.MedicoTerapistaRequest;
import com.piedrazul.appointments.medico.entity.MedicoTerapista;
import com.piedrazul.appointments.shared.enums.Especialidad;
import com.piedrazul.appointments.medico.repository.MedicoTerapistaRepository;
import com.piedrazul.appointments.shared.exception.RegistroException;
import com.piedrazul.appointments.shared.security.KeycloakAdminService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MedicoTerapistaService implements IMedicoTerapistaService {

    @Autowired
    private KeycloakAdminService keycloakAdminService;
    @Autowired
    private PasswordEncoder passwordEncoder;
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
    public MedicoTerapista guardar(MedicoTerapistaRequest request) {
        keycloakAdminService.crearUsuario(
                request.getUsername(),
                request.getPassword(),
                request.getNombres(),
                request.getApellidos(),
                "MEDICO_TERAPISTA");

        try {
            MedicoTerapista medico = new MedicoTerapista(
                    request.getUsername(),
                    passwordEncoder.encode(request.getPassword()),
                    request.getNombres(),
                    request.getApellidos(),
                    true,
                    request.getEspecialidad(),
                    request.getIntervaloCitas(),
                    request.getHoraInicio(),
                    request.getHoraFin());
            medico.setDiasAtencion(request.getDiasAtencion());

            return medicoTerapistaRepository.save(medico);

        } catch (Exception e) {
            keycloakAdminService.eliminarUsuario(request.getUsername());
            throw new RegistroException("Error al guardar el médico: " + e.getMessage());
        }
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
