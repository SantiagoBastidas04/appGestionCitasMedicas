package com.piedrazul.appointments.config;

import com.piedrazul.appointments.entities.ConfiguracionSistema;
import com.piedrazul.appointments.entities.MedicoTerapista;
import com.piedrazul.appointments.entities.Administrador;
import com.piedrazul.appointments.entities.Agendador;
import com.piedrazul.appointments.entities.enums.Especialidad;
import com.piedrazul.appointments.repositories.ConfiguracionSistemaRepository;
import com.piedrazul.appointments.repositories.MedicoTerapistaRepository;
import com.piedrazul.appointments.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final MedicoTerapistaRepository medicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder; // ← BCrypt inyectado
    private final ConfiguracionSistemaRepository configuracionRepository;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {

            // ── Administrador ─────────────────────────────────
            Administrador admin = new Administrador("admin", passwordEncoder.encode("agendador123"),
                    "Jose", "Rodriguez", true);
            usuarioRepository.save(admin);

            // ── Agendador ─────────────────────────────────────
            Agendador agendador = new Agendador(
                    "agendador", passwordEncoder.encode("agendador123"),
                    "Santiago", "Bastidas", true
            );
            usuarioRepository.save(agendador);

            // ── Médico 1 ──────────────────────────────────────
            MedicoTerapista medico1 = new MedicoTerapista(
                    "clara.cordoba",
                    passwordEncoder.encode("medico123"), // ← cifrado
                    "Clara",
                    "Ines Cordoba",
                    true,
                    Especialidad.FISIOTERAPIA,
                    15,
                    LocalTime.of(7, 0),
                    LocalTime.of(10, 0)
            );
            medico1.setDiasAtencion(Set.of(
                    DayOfWeek.MONDAY,
                    DayOfWeek.WEDNESDAY,
                    DayOfWeek.FRIDAY
            ));
            medicoRepository.save(medico1);

            // ── Médico 2 ──────────────────────────────────────
            MedicoTerapista medico2 = new MedicoTerapista(
                    "jose.garcia",
                    passwordEncoder.encode("medico123"), // ← cifrado
                    "Jose",
                    "Ignacio Garcia",
                    true,
                    Especialidad.QUIROPRAXIA,
                    20,
                    LocalTime.of(7, 0),
                    LocalTime.of(11, 0)
            );
            medico2.setDiasAtencion(Set.of(
                    DayOfWeek.TUESDAY,
                    DayOfWeek.THURSDAY
            ));
            medicoRepository.save(medico2);

            System.out.println("✅ Datos de prueba cargados correctamente");
        } else {
            System.out.println("ℹ️ Ya existen datos, omitiendo carga inicial");
        }
        if (configuracionRepository.count() == 0) {
            ConfiguracionSistema config = new ConfiguracionSistema(4);
            configuracionRepository.save(config);
            System.out.println("✅ Configuración del sistema cargada");
        }else{
            System.out.println("✅ Configuración del ya sistema cargada");
        }
    }
}