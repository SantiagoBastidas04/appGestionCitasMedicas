package com.piedrazul.appointments.shared.config;

import com.piedrazul.appointments.cita.entity.Cita;
import com.piedrazul.appointments.cita.repository.CitaRepository;
import com.piedrazul.appointments.configuracion.entity.ConfiguracionSistema;
import com.piedrazul.appointments.configuracion.repository.ConfiguracionSistemaRepository;
import com.piedrazul.appointments.medico.entity.MedicoTerapista;
import com.piedrazul.appointments.medico.repository.MedicoTerapistaRepository;
import com.piedrazul.appointments.paciente.entity.Paciente;
import com.piedrazul.appointments.paciente.repository.PacienteRepository;
import com.piedrazul.appointments.shared.entity.Administrador;
import com.piedrazul.appointments.shared.entity.Agendador;
import com.piedrazul.appointments.shared.entity.Usuario;
import com.piedrazul.appointments.shared.enums.EstadoCita;
import com.piedrazul.appointments.shared.enums.Genero;
import com.piedrazul.appointments.shared.repository.UsuarioRepository;
import com.piedrazul.appointments.shared.security.KeycloakAdminService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static com.piedrazul.appointments.shared.enums.Especialidad.*;

@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final KeycloakAdminService keycloakAdminService;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            log.info("=== DataLoader: BD ya tiene datos, omitiendo ===");
            return;
        }

        // Administrador
        crearUsuarioEnKeycloak("admin", "piedrazul123", "Jose", "Rodriguez", "ADMINISTRADOR");
        Administrador admin = usuarioRepository.save(
                new Administrador("admin", null, "Jose", "Rodriguez", true));

        // Agendadores
        crearUsuarioEnKeycloak("agendador", "piedrazul123", "Santiago", "Bastidas", "AGENDADOR");
        Agendador agendador1 = usuarioRepository.save(
                new Agendador("agendador", null, "Santiago", "Bastidas", true));

        crearUsuarioEnKeycloak("agendador2", "piedrazul123", "Laura", "Mosquera", "AGENDADOR");
        Agendador agendador2 = usuarioRepository.save(
                new Agendador("agendador2", null, "Laura", "Mosquera", true));

       

    }

    
    private void crearUsuarioEnKeycloak(String username, String password,
                                         String nombres, String apellidos, String rol) {
        try {
            keycloakAdminService.crearUsuario(username, password, nombres, apellidos, rol);
            log.info("Usuario '{}' creado en Keycloak", username);
        } catch (Exception e) {
            log.warn("Usuario '{}' ya existe en Keycloak o error: {}", username, e.getMessage());
        }
    }
}