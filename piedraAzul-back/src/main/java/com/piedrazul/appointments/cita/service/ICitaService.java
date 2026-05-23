package com.piedrazul.appointments.cita.service;

import com.piedrazul.appointments.cita.dto.CitaRequest;
import com.piedrazul.appointments.cita.entity.Cita;
import com.piedrazul.appointments.medico.entity.MedicoTerapista;
import com.piedrazul.appointments.shared.entity.Usuario;

import java.time.*;
import java.util.*;

public interface ICitaService {
    List<Cita> listarCitasPorMedicoYFecha(Long medicoId, LocalDate fecha);

    List<Cita> listarCitasPorPaciente(Long pacienteId);

    Cita crearCita(CitaRequest request, Usuario registradoPor);

    Cita reAgendarCita(Long citaId, LocalDate nuevaFecha, LocalTime nuevaHora, Usuario modificadoPor);

    List<LocalTime> calcularFranjasDisponibles(Long medicoId, LocalDate fecha);

    void validadDisponibilidad(MedicoTerapista medico, LocalDate fecha, LocalTime hora);

    Optional<Cita> buscarCitaPorId(Long id);

    Cita cancelarCita(Long id, Usuario usuario);

    String generarCSV(Long medicoId, LocalDate fecha, Usuario usuario);

}
