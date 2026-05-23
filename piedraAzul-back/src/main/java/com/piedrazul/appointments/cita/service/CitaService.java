package com.piedrazul.appointments.cita.service;

import com.piedrazul.appointments.cita.dto.CitaRequest;
import com.piedrazul.appointments.cita.entity.Cita;
import com.piedrazul.appointments.cita.entity.HistorialCita;
import com.piedrazul.appointments.cita.repository.CitaRepository;
import com.piedrazul.appointments.cita.repository.HistorialCitaRepository;
import com.piedrazul.appointments.medico.entity.MedicoTerapista;
import com.piedrazul.appointments.medico.repository.MedicoTerapistaRepository;
import com.piedrazul.appointments.paciente.entity.Paciente;
import com.piedrazul.appointments.paciente.repository.PacienteRepository;
import com.piedrazul.appointments.shared.enums.EstadoCita;
import com.piedrazul.appointments.shared.exception.AccesoDenegadoException;
import com.piedrazul.appointments.shared.exception.CitaInvalidaException;
import com.piedrazul.appointments.shared.entity.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service

public class CitaService implements ICitaService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoTerapistaRepository medicoTerapistaRepository;

    @Autowired
    private HistorialCitaRepository historialCitaRepository;

    @Override
    public List<Cita> listarCitasPorMedicoYFecha(Long medicoId, LocalDate fecha) {
        MedicoTerapista medico = medicoTerapistaRepository.findById(medicoId)
                .orElseThrow(() -> new RuntimeException("Medico no encontrado con el id: " + medicoId));
        return citaRepository.findByMedicoAndFechaAndEstadoNot(medico, fecha, EstadoCita.CANCELADA);
    }

    @Override
    public List<Cita> listarCitasPorPaciente(Long pacienteId) {
        return citaRepository.findByPacienteId(pacienteId);
    }

    @Override
    @Transactional
    public Cita reAgendarCita(Long citaId, LocalDate nuevaFecha, LocalTime nuevaHora, Usuario modificadoPor) {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con el id: " + citaId));
        HistorialCita historialCita = new HistorialCita(cita, cita.getFecha(), cita.getHora(), modificadoPor);

        if (modificadoPor instanceof MedicoTerapista medico &&
                !cita.getMedico().getId().equals(medico.getId())) {
            throw new AccesoDenegadoException("No puede reagendar citas de otro médico.");
        }
        // 1. No reagendar una cita cancelada
        if (cita.getEstado() == EstadoCita.CANCELADA) {
            throw new CitaInvalidaException(
                    "No se puede reagendar una cita que ya fue cancelada.");
        }
        // 2. Nueva fecha no puede ser pasada
        if (nuevaFecha.isBefore(LocalDate.now())) {
            throw new CitaInvalidaException(
                    "No se puede reagendar a una fecha pasada.");
        }
        // 3. Si es hoy, la hora no puede ser pasada
        if (nuevaFecha.isEqual(LocalDate.now()) &&
                nuevaHora.isBefore(LocalTime.now())) {
            throw new CitaInvalidaException(
                    "No se puede reagendar a una hora que ya pasó.");
        }

        // 4. Debe ser día de atención del médico
        if (!cita.getMedico().getDiasAtencion().contains(nuevaFecha.getDayOfWeek())) {
            throw new CitaInvalidaException(
                    "El médico no atiende el día " +
                            nuevaFecha.getDayOfWeek().toString().toLowerCase());
        }

        // 5. La hora debe estar dentro del horario
        if (nuevaHora.isBefore(cita.getMedico().getHoraInicio()) ||
                nuevaHora.isAfter(cita.getMedico().getHoraFin()
                        .minusMinutes(cita.getMedico().getIntervaloCitas()))) {
            throw new CitaInvalidaException(
                    "La hora está fuera del horario del médico. " +
                            "Horario: " + cita.getMedico().getHoraInicio() +
                            " - " + cita.getMedico().getHoraFin());
        }
        validadDisponibilidad(cita.getMedico(), nuevaFecha, nuevaHora);
        cita.setFecha(nuevaFecha);
        cita.setHora(nuevaHora);
        historialCitaRepository.save(historialCita);
        return citaRepository.save(cita);
    }

    @Override
    public List<LocalTime> calcularFranjasDisponibles(Long medicoId, LocalDate fecha) {
        MedicoTerapista medico = medicoTerapistaRepository.findById(medicoId)
                .orElseThrow(() -> new RuntimeException("Medico no encontrado con el id: " + medicoId));
        if (!medico.getDiasAtencion().contains(fecha.getDayOfWeek())) {
            return new ArrayList<>();
        }
        // Obtener horas ya ocupadas
        List<LocalTime> horasOcupadas = citaRepository.findHorasOcupadasByMedicoAndFecha(
                medico, fecha, EstadoCita.CANCELADA);

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
                    "El médico ya tiene una cita agendada para esa fecha y hora");
        }
    }

    @Override
    public Optional<Cita> buscarCitaPorId(Long id) {
        return citaRepository.findById(id);
    }

    @Override
    @Transactional
    public Cita cancelarCita(Long id, Usuario usuario) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con el id: " + id));

        if (usuario instanceof MedicoTerapista medico &&
                !cita.getMedico().getId().equals(medico.getId())) {
            throw new AccesoDenegadoException("No puede cancelar citas de otro médico.");
        }

        cita.setEstado(EstadoCita.CANCELADA);
        return citaRepository.save(cita);
    }

    @Override
    @Transactional
    public Cita crearCita(CitaRequest request, Usuario registradoPor) {

        MedicoTerapista medico = medicoTerapistaRepository.findById(request.getMedicoId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Médico no encontrado con id: " + request.getMedicoId()));

        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Paciente no encontrado con id: " + request.getPacienteId()));
        // 1. La fecha no puede ser pasada
        if (request.getFecha().isBefore(LocalDate.now())) {
            throw new CitaInvalidaException(
                    "No se puede agendar una cita en una fecha pasada.");
        }
        // 2. Si la fecha es hoy, la hora no puede ser pasada
        if (request.getFecha().isEqual(LocalDate.now()) &&
                request.getHora().isBefore(LocalTime.now())) {
            throw new CitaInvalidaException(
                    "No se puede agendar una cita en una hora que ya pasó.");
        }

        // 3. La fecha debe ser un día de atención del médico
        if (!medico.getDiasAtencion().contains(request.getFecha().getDayOfWeek())) {
            throw new CitaInvalidaException(
                    "El médico no atiende el día " +
                            request.getFecha().getDayOfWeek().toString().toLowerCase());
        }

        // 4. La hora debe estar dentro del horario del médico
        if (request.getHora().isBefore(medico.getHoraInicio()) ||
                request.getHora().isAfter(medico.getHoraFin().minusMinutes(medico.getIntervaloCitas()))) {
            throw new CitaInvalidaException(
                    "La hora está fuera del horario de atención del médico. " +
                            "Horario: " + medico.getHoraInicio() + " - " + medico.getHoraFin());
        }

        validadDisponibilidad(medico, request.getFecha(), request.getHora());

        Cita cita = new Cita(medico, paciente,
                request.getFecha(), request.getHora(),
                registradoPor, request.getObservacion());

        return citaRepository.save(cita);
    }

    @Override
    public String generarCSV(Long medicoId, LocalDate fecha, Usuario usuario) {

        if (usuario instanceof MedicoTerapista medico &&
                !medico.getId().equals(medicoId)) {
            throw new AccesoDenegadoException("No puede exportar citas de otro médico.");
        }
        List<Cita> citas = listarCitasPorMedicoYFecha(medicoId, fecha);

        StringBuilder csv = new StringBuilder();
        csv.append("Orden,Hora,Nombre Paciente,Documento,Celular,Estado,Observacion\n");

        int orden = 1;
        for (Cita cita : citas) {
            String nombre = cita.getPaciente().getNombres()
                    + " " + cita.getPaciente().getApellidos();
            String observacion = cita.getObservacion() != null
                    ? cita.getObservacion().replace(",", ";")
                    : "";

            csv.append(String.format("%d,%s,%s,%s,%s,%s,%s\n",
                    orden++,
                    cita.getHora().format(DateTimeFormatter.ofPattern("HH:mm")),
                    nombre,
                    cita.getPaciente().getNumeroDocumento(),
                    cita.getPaciente().getCelular(),
                    cita.getEstado().name(),
                    observacion));
        }
        return csv.toString();
    }
}
