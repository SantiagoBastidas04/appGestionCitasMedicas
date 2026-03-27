package com.piedrazul.appointments.services;

import com.piedrazul.appointments.entities.MedicoTerapista;
import com.piedrazul.appointments.entities.enums.Especialidad;
import com.piedrazul.appointments.repositories.MedicoTerapistaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicoTerapistaServiceTest {

    @Mock
    private MedicoTerapistaRepository medicoRepository;

    @InjectMocks
    private MedicoTerapistaService medicoService;

    private MedicoTerapista medico;

    @BeforeEach
    void setUp() {
        medico = new MedicoTerapista(
                "clara.cordoba",
                "password123",
                "Clara",
                "Ines Cordoba",
                true,
                Especialidad.FISIOTERAPIA,
                15,
                LocalTime.of(7, 0),
                LocalTime.of(10, 0)
        );
    }

    @Test
    void listarActivos_debeRetornarSoloMedicosActivos() {
        // ARRANGE
        when(medicoRepository.findByActivoTrue()).thenReturn(List.of(medico));

        // ACT
        List<MedicoTerapista> resultado = medicoService.listarActivos();

        // ASSERT
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).isActivo());
        verify(medicoRepository, times(1)).findByActivoTrue();
    }

    @Test
    void listarPorEspecialidad_debeRetornarMedicosFiltrados() {
        // ARRANGE
        when(medicoRepository.findByEspecialidadAndActivoTrue(Especialidad.FISIOTERAPIA))
                .thenReturn(List.of(medico));

        // ACT
        List<MedicoTerapista> resultado = medicoService
                .listarPorEspecialidad(Especialidad.FISIOTERAPIA);

        // ASSERT
        assertEquals(1, resultado.size());
        assertEquals(Especialidad.FISIOTERAPIA, resultado.get(0).getEspecialidad());
    }

    @Test
    void buscarPorId_cuandoExiste_debeRetornarMedico() {
        // ARRANGE
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));

        // ACT
        Optional<MedicoTerapista> resultado = medicoService.buscarPorId(1L);

        // ASSERT
        assertTrue(resultado.isPresent());
        assertEquals("Clara", resultado.get().getNombres());
    }

    @Test
    void cambiarEstado_cuandoMedicoExiste_debeDesactivar() {
        // ARRANGE
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(medicoRepository.save(any(MedicoTerapista.class))).thenReturn(medico);

        // ACT
        MedicoTerapista resultado = medicoService.cambiarEstado(1L, false);

        // ASSERT
        assertFalse(resultado.isActivo());
        verify(medicoRepository, times(1)).save(medico);
    }

    @Test
    void cambiarEstado_cuandoMedicoNoExiste_debeLanzarExcepcion() {
        // ARRANGE
        when(medicoRepository.findById(99L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> medicoService.cambiarEstado(99L, false)
        );
        verify(medicoRepository, never()).save(any());
    }
}