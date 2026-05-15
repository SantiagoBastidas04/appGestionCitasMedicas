package com.piedrazul.appointments.services;

import com.piedrazul.appointments.paciente.entity.Paciente;
import com.piedrazul.appointments.paciente.repository.PacienteRepository;
import com.piedrazul.appointments.paciente.service.PacienteService;
import com.piedrazul.appointments.shared.enums.Genero;
import com.piedrazul.appointments.shared.exception.CorreoDuplicadoException;
import com.piedrazul.appointments.shared.exception.DocumentoDuplicadoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
@DisplayName("Tests del PacienteService")
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private PacienteService pacienteService;

    private Paciente paciente;

    @BeforeEach
    void setUp() {
        paciente = new Paciente(
                "Ana Maria", "Lopez Torres", "1061234567",
                "3001234567", Genero.FEMENINO,
                LocalDate.of(1990, 5, 12), "ana@email.com"
        );
    }

    @Test
    @DisplayName("listarPacientes debe retornar todos los pacientes")
    void listarPacientes_debeRetornarListaDePacientes() {
        when(pacienteRepository.findAll()).thenReturn(List.of(paciente));
        List<Paciente> resultado = pacienteService.listarPacientes();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Ana Maria", resultado.get(0).getNombres());
        verify(pacienteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("listarPacientes cuando no hay pacientes debe retornar lista vacía")
    void listarPacientes_cuandoNoHayPacientes_debeRetornarListaVacia() {
        when(pacienteRepository.findAll()).thenReturn(List.of());
        assertTrue(pacienteService.listarPacientes().isEmpty());
    }

    @Test
    @DisplayName("buscarPacientePorId cuando existe debe retornar el paciente")
    void buscarPacientePorId_cuandoExiste_debeRetornarPaciente() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        Optional<Paciente> resultado = pacienteService.buscarPacientePorId(1L);
        assertTrue(resultado.isPresent());
        assertEquals("Lopez Torres", resultado.get().getApellidos());
    }

    @Test
    @DisplayName("buscarPacientePorId cuando no existe debe retornar vacío")
    void buscarPacientePorId_cuandoNoExiste_debeRetornarVacio() {
        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());
        assertFalse(pacienteService.buscarPacientePorId(99L).isPresent());
    }

    @Test
    @DisplayName("buscarPacientePorDocumento cuando existe debe retornar el paciente")
    void buscarPacientePorDocumento_cuandoExiste_debeRetornarPaciente() {
        when(pacienteRepository.findByNumeroDocumento("1061234567")).thenReturn(Optional.of(paciente));
        Optional<Paciente> resultado = pacienteService.buscarPacientePorNumDocumento("1061234567");
        assertTrue(resultado.isPresent());
        assertEquals("Ana Maria", resultado.get().getNombres());
    }

    @Test
    @DisplayName("buscarPacientePorDocumento cuando no existe debe retornar vacío")
    void buscarPacientePorDocumento_cuandoNoExiste_debeRetornarVacio() {
        when(pacienteRepository.findByNumeroDocumento("9999999999")).thenReturn(Optional.empty());
        assertFalse(pacienteService.buscarPacientePorNumDocumento("9999999999").isPresent());
    }

    @Test
    @DisplayName("guardarPaciente con datos únicos debe guardar correctamente")
    void guardarPaciente_cuandoDocumentoNoExiste_debeGuardar() {
        when(pacienteRepository.existsByNumeroDocumento("1061234567")).thenReturn(false);
        when(pacienteRepository.existsByCorreoElectronico("ana@email.com")).thenReturn(false);
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);

        Paciente resultado = pacienteService.guardarPaciente(paciente);

        assertNotNull(resultado);
        assertEquals("1061234567", resultado.getNumeroDocumento());
        verify(pacienteRepository, times(1)).save(paciente);
    }

    @Test
    @DisplayName("guardarPaciente con documento duplicado debe lanzar DocumentoDuplicadoException")
    void guardarPaciente_cuandoDocumentoYaExiste_debeLanzarDocumentoDuplicadoException() {
        when(pacienteRepository.existsByNumeroDocumento("1061234567")).thenReturn(true);

        DocumentoDuplicadoException ex = assertThrows(
                DocumentoDuplicadoException.class,
                () -> pacienteService.guardarPaciente(paciente)
        );

        assertTrue(ex.getMessage().contains("1061234567"));
        verify(pacienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("guardarPaciente con correo duplicado debe lanzar CorreoDuplicadoException")
    void guardarPaciente_cuandoCorreoYaExiste_debeLanzarCorreoDuplicadoException() {
        when(pacienteRepository.existsByNumeroDocumento("1061234567")).thenReturn(false);
        when(pacienteRepository.existsByCorreoElectronico("ana@email.com")).thenReturn(true);

        CorreoDuplicadoException ex = assertThrows(
                CorreoDuplicadoException.class,
                () -> pacienteService.guardarPaciente(paciente)
        );

        assertTrue(ex.getMessage().contains("ana@email.com"));
        verify(pacienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("guardarPaciente con correo nulo no debe validar correo duplicado")
    void guardarPaciente_cuandoCorreoEsNulo_noDebeValidarCorreo() {
        Paciente sinCorreo = new Paciente(
                "Carlos", "Ruiz", "9876543210",
                "3119876543", Genero.MASCULINO, null, null
        );
        when(pacienteRepository.existsByNumeroDocumento("9876543210")).thenReturn(false);
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(sinCorreo);

        Paciente resultado = pacienteService.guardarPaciente(sinCorreo);

        assertNotNull(resultado);
        verify(pacienteRepository, never()).existsByCorreoElectronico(any());
    }

    @Test
    @DisplayName("actualizarPaciente debe guardar los cambios correctamente")
    void actualizarPaciente_debeGuardarCambios() {
        paciente.setCelular("3159999999");
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);

        Paciente resultado = pacienteService.actualizarPaciente(paciente);

        assertNotNull(resultado);
        assertEquals("3159999999", resultado.getCelular());
        verify(pacienteRepository, times(1)).save(paciente);
    }

    @Test
    @DisplayName("buscarPacientePorUsername cuando existe debe retornar el paciente")
    void buscarPacientePorUsername_cuandoExiste_debeRetornarPaciente() {
        Paciente conUsername = new Paciente(
                "juanito", "pass", "Juan", "Gomez",
                "1234567890", "3001111111", true,
                Genero.MASCULINO, null, "juan@mail.com"
        );
        when(pacienteRepository.findByUsername("juanito")).thenReturn(Optional.of(conUsername));

        Optional<Paciente> resultado = pacienteService.buscarPacientePorUsername("juanito");

        assertTrue(resultado.isPresent());
        assertEquals("Juan", resultado.get().getNombres());
    }

    @Test
    @DisplayName("buscarPacientePorUsername cuando no existe debe retornar vacío")
    void buscarPacientePorUsername_cuandoNoExiste_debeRetornarVacio() {
        when(pacienteRepository.findByUsername("fantasma")).thenReturn(Optional.empty());
        assertFalse(pacienteService.buscarPacientePorUsername("fantasma").isPresent());
    }
}
