package com.piedrazul.appointments.services.interfaces;

import com.piedrazul.appointments.entities.MedicoTerapista;
import com.piedrazul.appointments.entities.enums.Especialidad;

import java.util.*;

public interface IMedicoTerapistaService {
    List<MedicoTerapista> listarActivos();
    List<MedicoTerapista> listarPorEspecialidad(Especialidad especialidad);
    Optional<MedicoTerapista> buscarPorId(Long id);
    MedicoTerapista guardar(MedicoTerapista medicoTerapista);
    MedicoTerapista cambiarEstado(Long id, boolean activo);

}
