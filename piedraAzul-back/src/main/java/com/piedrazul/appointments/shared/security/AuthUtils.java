package com.piedrazul.appointments.shared.security;

import com.piedrazul.appointments.shared.entity.Usuario;
import com.piedrazul.appointments.shared.repository.UsuarioRepository;
import com.piedrazul.appointments.shared.service.IUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtils {

    private final IUsuarioService usuarioService;

    public Usuario getUsuarioAutenticado() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        String username = jwt.getClaimAsString("preferred_username");

        return usuarioService.buscarPorUsername(username)
                .orElseThrow(() -> new RuntimeException(
                        "Usuario '" + username + "' autenticado en Keycloak pero no encontrado en BD. " +
                                "Verifica que el username en Keycloak coincida con el de la base de datos."));
    }
}