package com.piedrazul.appointments.shared.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


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
                        .hasAnyRole("ADMINISTRADOR", "AGENDADOR" , "MEDICO_TERAPISTA")
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
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOriginPattern("*");

        config.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));

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
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");

        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }
}