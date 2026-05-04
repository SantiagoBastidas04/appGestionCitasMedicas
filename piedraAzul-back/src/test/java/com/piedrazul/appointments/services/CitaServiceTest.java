package com.piedrazul.appointments.services;

import com.piedrazul.appointments.cita.controller.CitaController;
import com.piedrazul.appointments.cita.entity.Cita;
import com.piedrazul.appointments.cita.entity.HistorialCita;
import com.piedrazul.appointments.cita.repository.CitaRepository;
import com.piedrazul.appointments.cita.repository.HistorialCitaRepository;
import com.piedrazul.appointments.cita.service.CitaService;
import com.piedrazul.appointments.cita.service.ICitaService;
import com.piedrazul.appointments.medico.entity.MedicoTerapista;
import com.piedrazul.appointments.medico.repository.MedicoTerapistaRepository;
import com.piedrazul.appointments.paciente.entity.Paciente;
import com.piedrazul.appointments.shared.enums.Especialidad;
import com.piedrazul.appointments.shared.enums.EstadoCita;
import com.piedrazul.appointments.shared.enums.Genero;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitaServiceTest {

 
    @Mock private CitaRepository citaRepository;
    @Mock private MedicoTerapistaRepository medicoRepository;
    @Mock private HistorialCitaRepository historialCitaRepository;

    @Mock private ICitaService citaServiceMock;

    @InjectMocks private CitaController citaController;
    @InjectMocks private CitaService citaService;

    private MedicoTerapista medico;
    private Paciente paciente;
    private Cita cita;
    private LocalDate fecha;
    private LocalTime hora;

    @BeforeEach
    void setUp() {
        fecha = LocalDate.of(2026, 3, 23);
        hora  = LocalTime.of(7, 0);

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
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(citaRepository.findByMedicoAndFechaAndEstadoNot(medico, fecha, EstadoCita.CANCELADA))
                .thenReturn(List.of(cita));

        List<Cita> resultado = citaService.listarCitasPorMedicoYFecha(1L, fecha);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(fecha, resultado.get(0).getFecha());
    }

    @Test
    void crearCita_cuandoHoraDisponible_debeCrearCita() {
        when(citaRepository.existsByMedicoAndFechaAndHoraAndEstadoNot(
                medico, fecha, hora, EstadoCita.CANCELADA)).thenReturn(false);
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        Cita resultado = citaService.crearCita(medico, paciente, fecha, hora, medico, "observacion");

        assertNotNull(resultado);
        assertEquals(fecha, resultado.getFecha());
        assertEquals(hora, resultado.getHora());
        verify(citaRepository, times(1)).save(any(Cita.class));
    }

    @Test
    void crearCita_cuandoHoraOcupada_debeLanzarExcepcion() {
        when(citaRepository.existsByMedicoAndFechaAndHoraAndEstadoNot(
                medico, fecha, hora, EstadoCita.CANCELADA)).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> citaService.crearCita(medico, paciente, fecha, hora, medico, "observacion"));
        verify(citaRepository, never()).save(any());
    }

    @Test
    void calcularFranjasDisponibles_cuandoMedicoAtiende_debeRetornarFranjas() {
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(citaRepository.findHorasOcupadasByMedicoAndFecha(medico, fecha, EstadoCita.CANCELADA))
                .thenReturn(List.of(LocalTime.of(7, 0)));

        List<LocalTime> franjas = citaService.calcularFranjasDisponibles(1L, fecha);

        assertFalse(franjas.isEmpty());
        assertFalse(franjas.contains(LocalTime.of(7, 0)));
        assertTrue(franjas.contains(LocalTime.of(7, 15)));
    }

    @Test
    void calcularFranjasDisponibles_cuandoMedicoNoAtiende_debeRetornarVacio() {
        LocalDate martes = LocalDate.of(2026, 3, 24);
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));

        List<LocalTime> franjas = citaService.calcularFranjasDisponibles(1L, martes);

        assertTrue(franjas.isEmpty());
    }

    @Test
    void cancelarCita_cuandoExiste_debeCambiarEstado() {
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        Cita resultado = citaService.cancelarCita(1L);

        assertEquals(EstadoCita.CANCELADA, resultado.getEstado());
        verify(citaRepository, times(1)).save(cita);
    }

    @Test
    void reAgendarCita_cuandoNuevaHoraDisponible_debeActualizarCita() {
        LocalDate nuevaFecha = LocalDate.of(2026, 3, 25);
        LocalTime nuevaHora  = LocalTime.of(8, 0);

        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.existsByMedicoAndFechaAndHoraAndEstadoNot(
                medico, nuevaFecha, nuevaHora, EstadoCita.CANCELADA)).thenReturn(false);
        when(historialCitaRepository.save(any(HistorialCita.class)))
                .thenReturn(new HistorialCita(cita, fecha, hora, medico));
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        citaService.reAgendarCita(1L, nuevaFecha, nuevaHora, medico);

        verify(historialCitaRepository, times(1)).save(any(HistorialCita.class));
        verify(citaRepository, times(1)).save(cita);
    }


    @Test
    void exportarCitasCSV_cuandoExistenCitas_debeRetornarCSVCorrecto() {
        when(citaServiceMock.listarCitasPorMedicoYFecha(1L, fecha)).thenReturn(List.of(cita));

        ResponseEntity<String> response = citaController.exportarCitasCSV(1L, fecha);

        assertEquals(200, response.getStatusCodeValue());
        String body = response.getBody();
        assertNotNull(body);
        assertTrue(body.startsWith("Orden,Hora,Nombre Paciente,Documento,Celular,Estado,Observacion\n"));
        assertTrue(body.contains("Ana Lopez"));
        assertTrue(body.contains("07:00"));
        assertTrue(body.contains("1061234567"));
        assertTrue(body.contains("observacion"));
    }

    @Test
    void exportarCitasCSV_debeIncluirHeaderContentDisposition() {
        when(citaServiceMock.listarCitasPorMedicoYFecha(1L, fecha)).thenReturn(List.of(cita));

        ResponseEntity<String> response = citaController.exportarCitasCSV(1L, fecha);

        String contentDisposition = response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION);
        assertNotNull(contentDisposition);
        assertTrue(contentDisposition.contains("attachment"));
        assertTrue(contentDisposition.contains("citas_medico1_" + fecha + ".csv"));
    }

    @Test
    void exportarCitasCSV_debeRetornarContentTypeCsv() {
        when(citaServiceMock.listarCitasPorMedicoYFecha(1L, fecha)).thenReturn(List.of());

        ResponseEntity<String> response = citaController.exportarCitasCSV(1L, fecha);

        assertNotNull(response.getHeaders().getContentType());
        assertTrue(response.getHeaders().getContentType().toString().contains("text/csv"));
    }

    @Test
    void exportarCitasCSV_cuandoNoHayCitas_debeSoloRetornarEncabezado() {
        when(citaServiceMock.listarCitasPorMedicoYFecha(1L, fecha)).thenReturn(List.of());

        ResponseEntity<String> response = citaController.exportarCitasCSV(1L, fecha);

        String[] lineas = response.getBody().split("\n");
        assertEquals(1, lineas.length);
        assertEquals("Orden,Hora,Nombre Paciente,Documento,Celular,Estado,Observacion", lineas[0]);
    }

    @Test
    void exportarCitasCSV_cuandoObservacionTieneComas_debeSanitizarConPuntoComa() {
        Cita citaConComa = new Cita(medico, paciente, fecha, hora, medico, "tiene,una,coma");
        when(citaServiceMock.listarCitasPorMedicoYFecha(1L, fecha)).thenReturn(List.of(citaConComa));

        ResponseEntity<String> response = citaController.exportarCitasCSV(1L, fecha);

        String body = response.getBody();
        assertFalse(body.contains("tiene,una,coma"));
        assertTrue(body.contains("tiene;una;coma"));
    }

    @Test
    void exportarCitasCSV_cuandoObservacionEsNula_debeDejarCeldaVacia() {
        Cita citaSinObservacion = new Cita(medico, paciente, fecha, hora, medico, null);
        when(citaServiceMock.listarCitasPorMedicoYFecha(1L, fecha)).thenReturn(List.of(citaSinObservacion));

        ResponseEntity<String> response = citaController.exportarCitasCSV(1L, fecha);

        String lineaDatos = response.getBody().split("\n")[1];
        assertTrue(lineaDatos.endsWith(","));
    }

    @Test
    void exportarCitasCSV_cuandoVariasCitas_debeNumerarOrdenCorrectamente() {
        Cita cita2 = new Cita(medico, paciente, fecha, LocalTime.of(7, 15), medico, "obs2");
        when(citaServiceMock.listarCitasPorMedicoYFecha(1L, fecha)).thenReturn(List.of(cita, cita2));

        ResponseEntity<String> response = citaController.exportarCitasCSV(1L, fecha);

        String[] lineas = response.getBody().split("\n");
        assertEquals(3, lineas.length);
        assertTrue(lineas[1].startsWith("1,"));
        assertTrue(lineas[2].startsWith("2,"));
    }
}