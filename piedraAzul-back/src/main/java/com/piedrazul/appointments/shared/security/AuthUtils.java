package com.piedrazul.appointments.shared.security;

import com.piedrazul.appointments.shared.entity.Usuario;
import com.piedrazul.appointments.shared.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtils {

    private final UsuarioRepository usuarioRepository;

    // Obtiene el usuario autenticado desde el contexto de seguridad
    public Usuario getUsuarioAutenticado() {
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario autenticado no encontrado: " + username
                ));
    }
}