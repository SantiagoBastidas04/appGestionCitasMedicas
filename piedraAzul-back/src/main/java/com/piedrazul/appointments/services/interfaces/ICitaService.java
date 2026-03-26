package com.piedrazul.appointments.services.interfaces;

import com.piedrazul.appointments.entities.Cita;
import com.piedrazul.appointments.entities.MedicoTerapista;
import com.piedrazul.appointments.entities.Paciente;
import com.piedrazul.appointments.entities.Usuario;

import java.time.*;
import java.util.*;

public interface ICitaService {
    List<Cita> listarCitasPorMedicoYFecha(Long medicoId, LocalDate fecha);

    Cita crearCita(MedicoTerapista medico, Paciente paciente, LocalDate fecha, LocalTime hora, Usuario registradoPor, String observacion);

    Cita reAgendarCita(Long citaId, LocalDate nuevaFecha, LocalTime nuevaHora, Usuario modificadoPor);

    List<LocalTime> calcularFranjasDisponibles(Long medicoId, LocalDate fecha);

    void validadDisponibilidad(MedicoTerapista medico, LocalDate fecha, LocalTime hora);

    Optional<Cita> buscarCitaPorId(Long id);

    Cita cancelarCita(Long id);
}
