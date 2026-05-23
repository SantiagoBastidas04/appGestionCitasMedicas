package com.piedrazul.appointments.shared.service;

import com.piedrazul.appointments.shared.entity.Usuario;
import com.piedrazul.appointments.shared.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @Override
    public boolean existeUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    @Override
    @Transactional
    public Usuario guardar(Usuario usuario) {
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new IllegalArgumentException(
                    "Ya existe un usuario con el username: " + usuario.getUsername()
            );
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario cambiarEstado(Long id, boolean activo) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Usuario no encontrado con id: " + id
                ));
        usuario.setActivo(activo);
        return usuarioRepository.save(usuario);
    }
}