package com.piedrazul.appointments.cita.service;

import com.piedrazul.appointments.cita.entity.Cita;
import com.piedrazul.appointments.cita.entity.HistorialCita;
import com.piedrazul.appointments.cita.repository.CitaRepository;
import com.piedrazul.appointments.cita.repository.HistorialCitaRepository;
import com.piedrazul.appointments.medico.entity.MedicoTerapista;
import com.piedrazul.appointments.medico.repository.MedicoTerapistaRepository;
import com.piedrazul.appointments.paciente.entity.Paciente;
import com.piedrazul.appointments.shared.enums.EstadoCita;
import com.piedrazul.appointments.shared.entity.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
public class CitaService implements ICitaService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private MedicoTerapistaRepository medicoTerapistaRepository;

    @Autowired
    private HistorialCitaRepository historialCitaRepository;

    @Override
    public List<Cita> listarCitasPorMedicoYFecha(Long medicoId, LocalDate fecha) {
        MedicoTerapista medico = medicoTerapistaRepository.findById(medicoId).orElseThrow(() -> new RuntimeException("Medico no encontrado con el id: " + medicoId));
        return citaRepository.findByMedicoAndFechaAndEstadoNot(medico, fecha, EstadoCita.CANCELADA);
    }
    @Override
    public List<Cita> listarCitasPorPaciente(Long pacienteId) {
        return citaRepository.findByPacienteId(pacienteId);
    }
    @Override
    @Transactional
    public Cita crearCita(MedicoTerapista medico, Paciente paciente, LocalDate fecha, LocalTime hora, Usuario registradoPor, String observacion) {
        validadDisponibilidad(medico, fecha, hora);
        Cita cita = new Cita(medico, paciente, fecha, hora, registradoPor, observacion);
        return citaRepository.save(cita);
    }


    @Override
    @Transactional
    public Cita reAgendarCita(Long citaId, LocalDate nuevaFecha, LocalTime nuevaHora, Usuario modificadoPor) {
        Cita cita = citaRepository.findById(citaId).orElseThrow(() -> new RuntimeException("Cita no encontrada con el id: " + citaId));
        HistorialCita historialCita = new HistorialCita(cita, cita.getFecha(), cita.getHora(), modificadoPor);
        historialCitaRepository.save(historialCita);
        validadDisponibilidad(cita.getMedico(), nuevaFecha, nuevaHora);
        return citaRepository.save(cita);
    }

    @Override
    public List<LocalTime> calcularFranjasDisponibles(Long medicoId, LocalDate fecha) {
        MedicoTerapista medico = medicoTerapistaRepository.findById(medicoId).orElseThrow(() -> new RuntimeException("Medico no encontrado con el id: " + medicoId));
        if (!medico.getDiasAtencion().contains(fecha.getDayOfWeek())) {
            return new ArrayList<>();
        }
        // Obtener horas ya ocupadas
        List<LocalTime> horasOcupadas = citaRepository.findHorasOcupadasByMedicoAndFecha(
                medico, fecha, EstadoCita.CANCELADA
        );

        // Generar todas las franjas posibles según el intervalo
        List<LocalTime> franjasDisponibles = new ArrayList<>();
        LocalTime franja = medico.getHoraInicio();

        while (!franja.isAfter(medico.getHoraFin().minusMinutes(medico.getIntervaloCitas()))) {
            if (!horasOcupadas.contains(franja)) {
                franjasDisponibles.add(franja);
            }
            franja = franja.plusMinutes(medico.getIntervaloCitas());
        }

        return franjasDisponibles;
    }

    @Override
    public void validadDisponibilidad(MedicoTerapista medico, LocalDate fecha, LocalTime hora) {
        if (citaRepository.existsByMedicoAndFechaAndHoraAndEstadoNot(
                medico, fecha, hora, EstadoCita.CANCELADA)) {
            throw new IllegalArgumentException(
                    "El médico ya tiene una cita agendada para esa fecha y hora"
            );
        }
    }

    @Override
    public Optional<Cita> buscarCitaPorId(Long id) {
        return citaRepository.findById(id);
    }

    @Override
    @Transactional
    public Cita cancelarCita(Long id) {
        Cita cita = citaRepository.findById(id).orElseThrow(() -> new RuntimeException("Cita no encontrada con el id: " + id));
        cita.setEstado(EstadoCita.CANCELADA);
        return citaRepository.save(cita);
    }
}
