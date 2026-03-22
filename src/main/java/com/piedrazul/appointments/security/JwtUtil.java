package com.piedrazul.appointments.security;

import com.piedrazul.appointments.config.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
//genera y valida tokens
public class JwtUtil {

    private final JwtProperties jwtProperties;

    // Genera la clave secreta a partir del string en properties
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    // Genera un token JWT para un usuario
    public String generarToken(String username, String rol) {
        return Jwts.builder()
                .subject(username)
                .claim("rol", rol)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
                .signWith(getSecretKey())
                .compact();
    }

    // Extrae el username del token
    public String extraerUsername(String token) {
        return extraerClaims(token).getSubject();
    }

    // Extrae el rol del token
    public String extraerRol(String token) {
        return extraerClaims(token).get("rol", String.class);
    }

    // Valida que el token sea correcto y no haya expirado
    public boolean validarToken(String token) {
        try {
            extraerClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Extrae todas las partes del token
    private Claims extraerClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
