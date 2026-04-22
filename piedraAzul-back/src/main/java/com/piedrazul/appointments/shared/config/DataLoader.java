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
    private final MedicoTerapistaRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final CitaRepository citaRepository;
    private final ConfiguracionSistemaRepository configuracionRepository;

    @Override
    public void run(String... args) {
        // La contraseña de todos en piedrazul123, menos para admin (jose) y agendador (santiago) que sigue igual que antes
        if (usuarioRepository.count() > 0) {
            log.info("=== DataLoader: BD ya tiene datos, omitiendo carga inicial ===");
            return;
        }

        log.info("=== DataLoader: Cargando datos de prueba ===");

        //  Administrador
        Administrador admin = usuarioRepository.save(
                new Administrador("admin", null, "Jose", "Rodriguez", true));

        //  Agendadores
        Agendador agendador1 = usuarioRepository.save(
                new Agendador("agendador", null, "Santiago", "Bastidas", true));

        Agendador agendador2 = usuarioRepository.save(
                new Agendador("agendador2", null, "Laura", "Mosquera", true));

        //  Médicos
        MedicoTerapista clara = new MedicoTerapista(
                "clara.cordoba", null,
                "Clara", "Ines Cordoba", true,
                FISIOTERAPIA, 15,
                LocalTime.of(7, 0), LocalTime.of(12, 0));
        clara.setDiasAtencion(Set.of(
                DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        medicoRepository.save(clara);

        MedicoTerapista jose = new MedicoTerapista(
                "jose.garcia", null,
                "Jose", "Ignacio Garcia", true,
                QUIROPRAXIA, 20,
                LocalTime.of(7, 0), LocalTime.of(11, 0));
        jose.setDiasAtencion(Set.of(
                DayOfWeek.TUESDAY, DayOfWeek.THURSDAY));
        medicoRepository.save(jose);

        MedicoTerapista andrea = new MedicoTerapista(
                "andrea.torres", null,
                "Andrea", "Torres Muñoz", true,
                TERAPIA_NEURAL, 30,
                LocalTime.of(8, 0), LocalTime.of(13, 0));
        andrea.setDiasAtencion(Set.of(
                DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY));
        medicoRepository.save(andrea);

        //  Pacientes
        Paciente ana = pacienteRepository.save(new Paciente(
                "ana.lopez", null,
                "Ana Maria", "Lopez Torres", "1061234567", "3001234567",
                true, Genero.FEMENINO,
                LocalDate.of(1990, 5, 12), "ana.lopez@email.com"));

        Paciente carlos = pacienteRepository.save(new Paciente(
                "carlos.rincon", null,
                "Carlos", "Rincon Parra", "1060987654", "3109876543",
                true, Genero.MASCULINO,
                LocalDate.of(1985, 3, 22), "carlos.rincon@email.com"));

        Paciente maria = pacienteRepository.save(new Paciente(
                "maria.ospina", null,
                "Maria Fernanda", "Ospina Gutierrez", "1060111222", "3201112222",
                true, Genero.FEMENINO,
                LocalDate.of(1995, 8, 30), "maria.ospina@email.com"));

        Paciente pedro = pacienteRepository.save(new Paciente(
                "pedro.silva", null,
                "Pedro", "Silva Mendez", "1060333444", "3153334444",
                true, Genero.MASCULINO,
                LocalDate.of(1978, 11, 5), "pedro.silva@email.com"));

        Paciente lucia = pacienteRepository.save(new Paciente(
                "lucia.vargas", null,
                "Lucia", "Vargas Cardona", "1060555666", "3175556666",
                true, Genero.FEMENINO,
                LocalDate.of(2000, 2, 14), "lucia.vargas@email.com"));

        Paciente andres = pacienteRepository.save(new Paciente(
                "andres.mora", null,
                "Andres Felipe", "Mora Castillo", "1060777888", "3187778888",
                true, Genero.MASCULINO,
                LocalDate.of(1992, 7, 19), "andres.mora@email.com"));

        // Pacientes sin credenciales (registrados manualmente por el agendador)
        Paciente donaRosa = pacienteRepository.save(new Paciente(
                "Rosa", "Elena Perez",
                "1060999000", "3119990000",
                Genero.FEMENINO,
                LocalDate.of(1955, 4, 8),
                "rosa.perez@email.com"));

        Paciente donAlfonso = pacienteRepository.save(new Paciente(
                "Alfonso", "Gomez Ruiz",
                "1061001002", "3121001002",
                Genero.MASCULINO,
                LocalDate.of(1950, 9, 25),
                null));

        //  Citas — semana actual (lunes más cercano) ─
        // Calculamos fechas dinámicamente para que siempre sean relevantes
        LocalDate hoy = LocalDate.now();
        LocalDate lunesPasado  = hoy.with(DayOfWeek.MONDAY).minusWeeks(1);
        LocalDate martePasado  = lunesPasado.plusDays(1);
        LocalDate miercolPasado = lunesPasado.plusDays(2);
        LocalDate juevesPasado = lunesPasado.plusDays(3);
        LocalDate lunesActual  = hoy.with(DayOfWeek.MONDAY);
        LocalDate marteActual  = lunesActual.plusDays(1);
        LocalDate miercolActual = lunesActual.plusDays(2);
        LocalDate juevesActual = lunesActual.plusDays(3);
        LocalDate viernesActual = lunesActual.plusDays(4);
        LocalDate lunesProximo = lunesActual.plusWeeks(1);
        LocalDate marteProximo = lunesProximo.plusDays(1);

        //  Citas de Clara (Fisioterapia) — Lun/Mie/Vie, cada 15 min ─
        crearCita(clara, ana,      lunesPasado,  LocalTime.of(7, 0),  agendador1, "Dolor lumbar crónico",     EstadoCita.COMPLETADA);
        crearCita(clara, carlos,   lunesPasado,  LocalTime.of(7, 15), agendador1, "Rehabilitación rodilla",   EstadoCita.COMPLETADA);
        crearCita(clara, maria,    lunesPasado,  LocalTime.of(7, 30), agendador1, null,                       EstadoCita.COMPLETADA);
        crearCita(clara, pedro,    lunesPasado,  LocalTime.of(7, 45), agendador1, "Post-operatorio",          EstadoCita.CANCELADA);
        crearCita(clara, lucia,    miercolPasado,LocalTime.of(7, 0),  agendador2, "Tensión cervical",         EstadoCita.COMPLETADA);
        crearCita(clara, andres,   miercolPasado,LocalTime.of(7, 15), agendador2, null,                       EstadoCita.COMPLETADA);
        crearCita(clara, donaRosa, miercolPasado,LocalTime.of(7, 30), agendador1, "Control mensual",          EstadoCita.COMPLETADA);

        crearCita(clara, ana,      lunesActual,  LocalTime.of(7, 0),  agendador1, "Seguimiento lumbar",       EstadoCita.CONFIRMADA);
        crearCita(clara, carlos,   lunesActual,  LocalTime.of(7, 15), agendador1, null,                       EstadoCita.CONFIRMADA);
        crearCita(clara, pedro,    lunesActual,  LocalTime.of(7, 30), agendador2, "Electroestimulación",      EstadoCita.CONFIRMADA);
        crearCita(clara, donAlfonso,lunesActual, LocalTime.of(7, 45), agendador1, "Primera consulta",         EstadoCita.CONFIRMADA);
        crearCita(clara, maria,    miercolActual,LocalTime.of(7, 0),  agendador1, null,                       EstadoCita.CONFIRMADA);
        crearCita(clara, lucia,    miercolActual,LocalTime.of(7, 15), agendador2, "Dolor hombro derecho",     EstadoCita.CONFIRMADA);
        crearCita(clara, andres,   miercolActual,LocalTime.of(7, 30), agendador1, null,                       EstadoCita.CONFIRMADA);
        crearCita(clara, ana,      viernesActual,LocalTime.of(7, 0),  agendador1, "Control semanal",          EstadoCita.CONFIRMADA);
        crearCita(clara, donaRosa, viernesActual,LocalTime.of(7, 15), agendador2, null,                       EstadoCita.CONFIRMADA);

        crearCita(clara, carlos,   lunesProximo, LocalTime.of(7, 0),  agendador1, "Continuación tratamiento", EstadoCita.CONFIRMADA);
        crearCita(clara, maria,    lunesProximo, LocalTime.of(7, 15), agendador1, null,                       EstadoCita.CONFIRMADA);

        //  Citas de Jose (Quiropraxia) — Mar/Jue, cada 20 min ─
        crearCita(jose, pedro,    martePasado,  LocalTime.of(7, 0),  agendador1, "Ajuste columna",            EstadoCita.COMPLETADA);
        crearCita(jose, lucia,    martePasado,  LocalTime.of(7, 20), agendador2, "Dolor cervical",            EstadoCita.COMPLETADA);
        crearCita(jose, donAlfonso,martePasado, LocalTime.of(7, 40), agendador1, "Primera visita",            EstadoCita.COMPLETADA);
        crearCita(jose, ana,      juevesPasado, LocalTime.of(7, 0),  agendador1, null,                        EstadoCita.CANCELADA);
        crearCita(jose, carlos,   juevesPasado, LocalTime.of(7, 20), agendador2, "Escoliosis leve",           EstadoCita.COMPLETADA);

        crearCita(jose, andres,   marteActual,  LocalTime.of(7, 0),  agendador1, "Ajuste mensual",            EstadoCita.CONFIRMADA);
        crearCita(jose, maria,    marteActual,  LocalTime.of(7, 20), agendador1, null,                        EstadoCita.CONFIRMADA);
        crearCita(jose, pedro,    marteActual,  LocalTime.of(7, 40), agendador2, "Seguimiento",               EstadoCita.CONFIRMADA);
        crearCita(jose, donaRosa, marteActual,  LocalTime.of(8, 0),  agendador1, "Dolor cadera",              EstadoCita.CONFIRMADA);
        crearCita(jose, lucia,    juevesActual, LocalTime.of(7, 0),  agendador2, null,                        EstadoCita.CONFIRMADA);
        crearCita(jose, ana,      juevesActual, LocalTime.of(7, 20), agendador1, "Control quincenal",         EstadoCita.CONFIRMADA);
        crearCita(jose, donAlfonso,juevesActual,LocalTime.of(7, 40), agendador1, "Hernia discal L4-L5",       EstadoCita.CONFIRMADA);

        crearCita(jose, carlos,   marteProximo, LocalTime.of(7, 0),  agendador1, null,                        EstadoCita.CONFIRMADA);
        crearCita(jose, andres,   marteProximo, LocalTime.of(7, 20), agendador2, "Post ajuste",               EstadoCita.CONFIRMADA);

        //  Citas de Andrea (Terapia Neural) — Lun a Vie, cada 30 min
        crearCita(andrea, lucia,   lunesPasado,  LocalTime.of(8, 0),  agendador2, "Migraña crónica",          EstadoCita.COMPLETADA);
        crearCita(andrea, andres,  lunesPasado,  LocalTime.of(8, 30), agendador1, null,                       EstadoCita.COMPLETADA);
        crearCita(andrea, donaRosa,martePasado,  LocalTime.of(8, 0),  agendador1, "Neuralgia facial",         EstadoCita.COMPLETADA);
        crearCita(andrea, ana,     miercolPasado,LocalTime.of(8, 0),  agendador2, "Fibromialgia",             EstadoCita.COMPLETADA);
        crearCita(andrea, carlos,  juevesPasado, LocalTime.of(8, 0),  agendador1, "Dolor crónico difuso",     EstadoCita.CANCELADA);

        crearCita(andrea, pedro,   lunesActual,  LocalTime.of(8, 0),  agendador1, "Primera sesión neural",    EstadoCita.CONFIRMADA);
        crearCita(andrea, maria,   lunesActual,  LocalTime.of(8, 30), agendador2, null,                       EstadoCita.CONFIRMADA);
        crearCita(andrea, lucia,   marteActual,  LocalTime.of(8, 0),  agendador1, "Seguimiento migraña",      EstadoCita.CONFIRMADA);
        crearCita(andrea, donAlfonso,marteActual,LocalTime.of(8, 30), agendador1, "Nervio ciático",           EstadoCita.CONFIRMADA);
        crearCita(andrea, andres,  miercolActual,LocalTime.of(8, 0),  agendador2, null,                       EstadoCita.CONFIRMADA);
        crearCita(andrea, ana,     miercolActual,LocalTime.of(8, 30), agendador1, "Control fibromialgia",     EstadoCita.CONFIRMADA);
        crearCita(andrea, carlos,  juevesActual, LocalTime.of(8, 0),  agendador1, "Sesión 3 de 6",            EstadoCita.CONFIRMADA);
        crearCita(andrea, donaRosa,viernesActual,LocalTime.of(8, 0),  agendador2, "Neuralgia seguimiento",    EstadoCita.CONFIRMADA);
        crearCita(andrea, maria,   viernesActual,LocalTime.of(8, 30), agendador1, null,                       EstadoCita.CONFIRMADA);

        //  Configuración del sistema
        configuracionRepository.save(new ConfiguracionSistema(4));

        log.info("=== DataLoader: Carga completada ===");
        log.info("  Usuarios:  2 admins/agendadores, 3 médicos");
        log.info("  Pacientes: 8 (6 con credenciales, 2 sin credenciales)");
        log.info("  Citas:     ~45 distribuidas entre los 3 médicos");
    }

    private void crearCita(MedicoTerapista medico, Paciente paciente,
                           LocalDate fecha, LocalTime hora,
                           Usuario registradoPor, String observacion,
                           EstadoCita estado) {
        Cita cita = new Cita(medico, paciente, fecha, hora, registradoPor, observacion);
        cita.setEstado(estado);
        citaRepository.save(cita);
    }
}