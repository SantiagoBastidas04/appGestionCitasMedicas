package com.piedrazul.appointments.medico.service;

import com.piedrazul.appointments.medico.dto.MedicoTerapistaRequest;
import com.piedrazul.appointments.medico.entity.MedicoTerapista;
import com.piedrazul.appointments.shared.enums.Especialidad;

import java.util.*;

public interface IMedicoTerapistaService {
    List<MedicoTerapista> listarActivos();
    List<MedicoTerapista> listarPorEspecialidad(Especialidad especialidad);
    Optional<MedicoTerapista> buscarPorId(Long id);
    MedicoTerapista guardar(MedicoTerapistaRequest request);
    MedicoTerapista cambiarEstado(Long id, boolean activo);

}
