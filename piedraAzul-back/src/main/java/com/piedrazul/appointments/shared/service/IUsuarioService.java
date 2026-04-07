package com.piedrazul.appointments.shared.service;

import com.piedrazul.appointments.shared.entity.Usuario;
import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    List<Usuario> listarUsuarios();
    Optional<Usuario> buscarPorId(Long id);
    Optional<Usuario> buscarPorUsername(String username);
    boolean existeUsername(String username);
    Usuario guardar(Usuario usuario);
    Usuario cambiarEstado(Long id, boolean activo);
}
