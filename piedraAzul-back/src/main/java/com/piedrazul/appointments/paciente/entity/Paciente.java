package com.piedrazul.appointments.paciente.entity;

import java.time.LocalDate;

import com.piedrazul.appointments.shared.entity.Usuario;
import com.piedrazul.appointments.shared.enums.Genero;
import com.piedrazul.appointments.shared.enums.RolUsuario;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "pacientes")
@DiscriminatorValue("PACIENTE")
public class Paciente extends Usuario {

    @Column(nullable = false, unique = true)
    private String numeroDocumento;

    @Column(nullable = false)
    private String celular;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genero genero;

    private LocalDate fechaNacimiento;

    private String correoElectronico;

    public Paciente(String username,
                    String password,
                    String nombre,
                    String apellido,
                    String numeroDocumento,
                    String celular,
                    Boolean activo,
                    Genero genero,
                    LocalDate fechaNacimiento,
                    String correoElectronico) {
        super(username, password, nombre, apellido, activo, RolUsuario.PACIENTE);
        this.numeroDocumento = numeroDocumento;
        this.celular = celular;
        this.genero = genero;
        this.fechaNacimiento = fechaNacimiento;
        this.correoElectronico = correoElectronico;
    }

    // Constructor para agendamiento manual — el agendador registra al paciente
    public Paciente(String nombre,
                    String apellido,
                    String numeroDocumento,
                    String celular,
                    Genero genero,
                    LocalDate fechaNacimiento,
                    String correoElectronico) {
        super(null, null, nombre, apellido, false, RolUsuario.PACIENTE);
        this.numeroDocumento = numeroDocumento;
        this.celular = celular;
        this.genero = genero;
        this.fechaNacimiento = fechaNacimiento;
        this.correoElectronico = correoElectronico;
    }
}
