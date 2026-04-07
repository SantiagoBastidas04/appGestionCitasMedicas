package com.piedrazul.appointments.services;

import com.piedrazul.appointments.paciente.entity.Paciente;
import com.piedrazul.appointments.paciente.service.PacienteService;
import com.piedrazul.appointments.shared.enums.Genero;
import com.piedrazul.appointments.paciente.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private PacienteService pacienteService;

    private Paciente paciente;

    @BeforeEach
    void setUp() {
        // Datos de prueba reutilizables en todos los tests
        paciente = new Paciente(
                "Ana Maria",
                "Lopez Torres",
                "1061234567",
                "3001234567",
                Genero.FEMENINO,
                LocalDate.of(1990, 5, 12),
                "ana@email.com"
        );
    }

    @Test
    void listarPacientes_debeRetornarListaDePacientes() {
        // ARRANGE
        when(pacienteRepository.findAll()).thenReturn(List.of(paciente));

        // ACT
        List<Paciente> resultado = pacienteService.listarPacientes();

        // ASSERT
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Ana Maria", resultado.get(0).getNombres());
        verify(pacienteRepository, times(1)).findAll();
    }

    @Test
    void buscarPacientePorId_cuandoExiste_debeRetornarPaciente() {
        // ARRANGE
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        // ACT
        Optional<Paciente> resultado = pacienteService.buscarPacientePorId(1L);

        // ASSERT
        assertTrue(resultado.isPresent());
        assertEquals("Lopez Torres", resultado.get().getApellidos());
    }

    @Test
    void buscarPacientePorId_cuandoNoExiste_debeRetornarVacio() {
        // ARRANGE
        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());

        // ACT
        Optional<Paciente> resultado = pacienteService.buscarPacientePorId(99L);

        // ASSERT
        assertFalse(resultado.isPresent());
    }

    @Test
    void guardarPaciente_cuandoDocumentoNoExiste_debeGuardar() {
        // ARRANGE
        when(pacienteRepository.existsByNumeroDocumento("1061234567"))
                .thenReturn(false);
        when(pacienteRepository.save(any(Paciente.class)))
                .thenReturn(paciente);

        // ACT
        Paciente resultado = pacienteService.guardarPaciente(paciente);

        // ASSERT
        assertNotNull(resultado);
        assertEquals("1061234567", resultado.getNumeroDocumento());
        verify(pacienteRepository, times(1)).save(paciente);
    }

    @Test
    void guardarPaciente_cuandoDocumentoYaExiste_debeLanzarExcepcion() {
        // ARRANGE
        when(pacienteRepository.existsByNumeroDocumento("1061234567"))
                .thenReturn(true);

        // ACT & ASSERT
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pacienteService.guardarPaciente(paciente)
        );

        assertEquals(
                "Ya existe un paciente con el documento: 1061234567",
                exception.getMessage()
        );
        verify(pacienteRepository, never()).save(any());
    }

    @Test
    void buscarPacientePorDocumento_cuandoExiste_debeRetornarPaciente() {
        // ARRANGE
        when(pacienteRepository.findByNumeroDocumento("1061234567"))
                .thenReturn(Optional.of(paciente));

        // ACT
        Optional<Paciente> resultado = pacienteService
                .buscarPacientePorNumDocumento("1061234567");

        // ASSERT
        assertTrue(resultado.isPresent());
        assertEquals("Ana Maria", resultado.get().getNombres());
    }
}