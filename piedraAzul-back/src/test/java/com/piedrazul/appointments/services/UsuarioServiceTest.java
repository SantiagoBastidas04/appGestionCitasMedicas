package com.piedrazul.appointments.services;

import com.piedrazul.appointments.entities.Administrador;
import com.piedrazul.appointments.entities.Usuario;
import com.piedrazul.appointments.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Administrador(
                "admin",
                "admin123",
                "Jose",
                "Rodriguez",
                true
        );
    }

    @Test
    void listarUsuarios_debeRetornarListaDeUsuarios() {
        // ARRANGE
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        // ACT
        List<Usuario> resultado = usuarioService.listarUsuarios();

        // ASSERT
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void buscarPorUsername_cuandoExiste_debeRetornarUsuario() {
        // ARRANGE
        when(usuarioRepository.findByUsername("admin"))
                .thenReturn(Optional.of(usuario));

        // ACT
        Optional<Usuario> resultado = usuarioService.buscarPorUsername("admin");

        // ASSERT
        assertTrue(resultado.isPresent());
        assertEquals("admin", resultado.get().getUsername());
    }

    @Test
    void guardar_cuandoUsernameNoExiste_debeGuardar() {
        // ARRANGE
        when(usuarioRepository.existsByUsername("admin")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // ACT
        Usuario resultado = usuarioService.guardar(usuario);

        // ASSERT
        assertNotNull(resultado);
        assertEquals("admin", resultado.getUsername());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void guardar_cuandoUsernameYaExiste_debeLanzarExcepcion() {
        // ARRANGE
        when(usuarioRepository.existsByUsername("admin")).thenReturn(true);

        // ACT & ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> usuarioService.guardar(usuario)
        );
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void cambiarEstado_cuandoUsuarioExiste_debeDesactivar() {
        // ARRANGE
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // ACT
        Usuario resultado = usuarioService.cambiarEstado(1L, false);

        // ASSERT
        assertFalse(resultado.isActivo());
        verify(usuarioRepository, times(1)).save(usuario);
    }
}