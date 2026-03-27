package com.piedrazul.appointments.services;

import com.piedrazul.appointments.entities.*;
import com.piedrazul.appointments.entities.enums.*;
import com.piedrazul.appointments.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitaServiceTest {

    @Mock
    private CitaRepository citaRepository;

    @Mock
    private MedicoTerapistaRepository medicoRepository;

    @Mock
    private HistorialCitaRepository historialCitaRepository;

    @InjectMocks
    private CitaService citaService;

    private MedicoTerapista medico;
    private Paciente paciente;
    private Cita cita;
    private LocalDate fecha;
    private LocalTime hora;

    @BeforeEach
    void setUp() {
        fecha = LocalDate.of(2026, 3, 23); // lunes
        hora = LocalTime.of(7, 0);

        medico = new MedicoTerapista(
                "clara.cordoba", "pass", "Clara", "Ines Cordoba",
                true, Especialidad.FISIOTERAPIA, 15,
                LocalTime.of(7, 0), LocalTime.of(10, 0)
        );
        medico.setDiasAtencion(Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY));

        paciente = new Paciente(
                "Ana", "Lopez", "1061234567", "3001234567",
                Genero.FEMENINO, null, null
        );

        cita = new Cita(medico, paciente, fecha, hora, medico, "observacion");
    }

    @Test
    void listarCitasPorMedicoYFecha_debeRetornarCitas() {
        // ARRANGE
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(citaRepository.findByMedicoAndFechaAndEstadoNot(
                medico, fecha, EstadoCita.CANCELADA))
                .thenReturn(List.of(cita));

        // ACT
        List<Cita> resultado = citaService.listarCitasPorMedicoYFecha(1L, fecha);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(fecha, resultado.get(0).getFecha());
    }

    @Test
    void crearCita_cuandoHoraDisponible_debeCrearCita() {
        // ARRANGE
        when(citaRepository.existsByMedicoAndFechaAndHoraAndEstadoNot(
                medico, fecha, hora, EstadoCita.CANCELADA))
                .thenReturn(false);
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        // ACT
        Cita resultado = citaService.crearCita(
                medico, paciente, fecha, hora, medico, "observacion"
        );

        // ASSERT
        assertNotNull(resultado);
        assertEquals(fecha, resultado.getFecha());
        assertEquals(hora, resultado.getHora());
        verify(citaRepository, times(1)).save(any(Cita.class));
    }

    @Test
    void crearCita_cuandoHoraOcupada_debeLanzarExcepcion() {
        // ARRANGE
        when(citaRepository.existsByMedicoAndFechaAndHoraAndEstadoNot(
                medico, fecha, hora, EstadoCita.CANCELADA))
                .thenReturn(true);

        // ACT & ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> citaService.crearCita(
                        medico, paciente, fecha, hora, medico, "observacion"
                )
        );
        verify(citaRepository, never()).save(any());
    }

    @Test
    void calcularFranjasDisponibles_cuandoMedicoAtiende_debeRetornarFranjas() {
        // ARRANGE — fecha es lunes, médico atiende lunes
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(citaRepository.findHorasOcupadasByMedicoAndFecha(
                medico, fecha, EstadoCita.CANCELADA))
                .thenReturn(List.of(LocalTime.of(7, 0))); // 07:00 ocupada

        // ACT
        List<LocalTime> franjas = citaService.calcularFranjasDisponibles(1L, fecha);

        // ASSERT
        assertFalse(franjas.isEmpty());
        assertFalse(franjas.contains(LocalTime.of(7, 0))); // ocupada no aparece
        assertTrue(franjas.contains(LocalTime.of(7, 15))); // libre sí aparece
    }

    @Test
    void calcularFranjasDisponibles_cuandoMedicoNoAtiende_debeRetornarVacio() {
        // ARRANGE — fecha es martes, médico solo atiende lunes y miércoles
        LocalDate martes = LocalDate.of(2026, 3, 24);
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));

        // ACT
        List<LocalTime> franjas = citaService.calcularFranjasDisponibles(1L, martes);

        // ASSERT
        assertTrue(franjas.isEmpty());
    }

    @Test
    void cancelarCita_cuandoExiste_debeCambiarEstado() {
        // ARRANGE
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        // ACT
        Cita resultado = citaService.cancelarCita(1L);

        // ASSERT
        assertEquals(EstadoCita.CANCELADA, resultado.getEstado());
        verify(citaRepository, times(1)).save(cita);
    }

    @Test
    void reAgendarCita_cuandoNuevaHoraDisponible_debeActualizarCita() {
        // ARRANGE
        LocalDate nuevaFecha = LocalDate.of(2026, 3, 25); // miércoles
        LocalTime nuevaHora = LocalTime.of(8, 0);

        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.existsByMedicoAndFechaAndHoraAndEstadoNot(
                medico, nuevaFecha, nuevaHora, EstadoCita.CANCELADA))
                .thenReturn(false);
        when(historialCitaRepository.save(any(HistorialCita.class)))
                .thenReturn(new HistorialCita(cita, fecha, hora, medico));
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        // ACT
        Cita resultado = citaService.reAgendarCita(1L, nuevaFecha, nuevaHora, medico);

        // ASSERT
        verify(historialCitaRepository, times(1)).save(any(HistorialCita.class));
        verify(citaRepository, times(1)).save(cita);
    }
}