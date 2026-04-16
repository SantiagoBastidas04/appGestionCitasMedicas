package com.piedrazul.appointments.shared.config;

import com.piedrazul.appointments.shared.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()

                        // Pacientes
                        .requestMatchers(HttpMethod.POST, "/api/pacientes")
                        .hasAnyRole("AGENDADOR", "ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/api/pacientes/username/**")
                        .hasAnyRole("AGENDADOR", "ADMINISTRADOR", "MEDICO_TERAPISTA", "PACIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/pacientes/**")
                        .hasAnyRole("AGENDADOR", "ADMINISTRADOR", "MEDICO_TERAPISTA")
                        .requestMatchers(HttpMethod.PUT, "/api/pacientes/**")
                        .hasAnyRole("AGENDADOR", "ADMINISTRADOR")

                        // Médicos
                        .requestMatchers(HttpMethod.POST, "/api/medicos")
                        .hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/medicos/**")
                        .hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/api/medicos/**")
                        .hasAnyRole("ADMINISTRADOR", "AGENDADOR", "MEDICO_TERAPISTA", "PACIENTE")

                        // Citas
                        .requestMatchers(HttpMethod.GET, "/api/citas/*/franjas")
                        .hasAnyRole("ADMINISTRADOR", "AGENDADOR", "MEDICO_TERAPISTA", "PACIENTE")
                        .requestMatchers(HttpMethod.POST, "/api/citas")
                        .hasAnyRole("ADMINISTRADOR", "AGENDADOR", "PACIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/citas/medico/**")
                        .hasAnyRole("ADMINISTRADOR", "AGENDADOR", "MEDICO_TERAPISTA")
                        .requestMatchers(HttpMethod.PUT, "/api/citas/*/reagendar")
                        .hasAnyRole("ADMINISTRADOR", "AGENDADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/citas/**")
                        .hasAnyRole("ADMINISTRADOR", "AGENDADOR")
                        .requestMatchers(HttpMethod.GET, "/api/citas/paciente/**")
                        .hasAnyRole("ADMINISTRADOR", "AGENDADOR", "MEDICO_TERAPISTA", "PACIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/citas/*/export/csv")
                        .hasAnyRole("ADMINISTRADOR", "AGENDADOR", "MEDICO_TERAPISTA")
                        // Configuración
                        .requestMatchers("/api/configuracion/**")
                        .hasRole("ADMINISTRADOR")
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Acepta CUALQUIER origen — útil en desarrollo
        // En producción reemplazar por la URL real del frontend
        config.addAllowedOriginPattern("*");

        config.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));

        // Acepta todos los headers, incluyendo Authorization
        config.addAllowedHeader("*");

        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(false);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}