package com.piedrazul.appointments.dto;

import com.piedrazul.appointments.auth.dto.RegistroPacienteRequest;
import com.piedrazul.appointments.shared.enums.Genero;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de validación del RegistroPacienteRequest")
class RegistroPacienteRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    private RegistroPacienteRequest requestValido() {
        RegistroPacienteRequest r = new RegistroPacienteRequest();
        r.setUsername("juanito");
        r.setPassword("123456");
        r.setNombres("Juan Carlos");
        r.setApellidos("Perez Lopez");
        r.setNumeroDocumento("1234567890");
        r.setCelular("3121234567");
        r.setGenero(Genero.MASCULINO);
        r.setFechaNacimiento(LocalDate.of(1995, 5, 10));
        r.setCorreoElectronico("juan@mail.com");
        return r;
    }

  

    @Test
    @DisplayName("Request con todos los campos válidos no debe tener violaciones")
    void requestValido_noDebeViolaciones() {
        Set<ConstraintViolation<RegistroPacienteRequest>> violations =
                validator.validate(requestValido());
        assertTrue(violations.isEmpty());
    }

  
    @Test
    @DisplayName("Celular con letras debe fallar la validación")
    void celular_conLetras_debeViolacion() {
        RegistroPacienteRequest r = requestValido();
        r.setCelular("312abc1234");

        Set<ConstraintViolation<RegistroPacienteRequest>> violations = validator.validate(r);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("celular")));
    }

    @Test
    @DisplayName("Celular con menos de 10 dígitos debe fallar la validación")
    void celular_conMenosDe10Digitos_debeViolacion() {
        RegistroPacienteRequest r = requestValido();
        r.setCelular("312123");

        Set<ConstraintViolation<RegistroPacienteRequest>> violations = validator.validate(r);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("celular")));
    }

    @Test
    @DisplayName("Celular con más de 10 dígitos debe fallar la validación")
    void celular_conMasDe10Digitos_debeViolacion() {
        RegistroPacienteRequest r = requestValido();
        r.setCelular("31212345678901");

        Set<ConstraintViolation<RegistroPacienteRequest>> violations = validator.validate(r);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("celular")));
    }

    @Test
    @DisplayName("Celular vacío debe fallar la validación")
    void celular_vacio_debeViolacion() {
        RegistroPacienteRequest r = requestValido();
        r.setCelular("");

        Set<ConstraintViolation<RegistroPacienteRequest>> violations = validator.validate(r);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("celular")));
    }


    @Test
    @DisplayName("Documento con letras debe fallar la validación")
    void documento_conLetras_debeViolacion() {
        RegistroPacienteRequest r = requestValido();
        r.setNumeroDocumento("ABC1234567");

        Set<ConstraintViolation<RegistroPacienteRequest>> violations = validator.validate(r);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("numeroDocumento")));
    }

    @Test
    @DisplayName("Documento con menos de 10 dígitos debe fallar la validación")
    void documento_conMenosDe10Digitos_debeViolacion() {
        RegistroPacienteRequest r = requestValido();
        r.setNumeroDocumento("12345");

        Set<ConstraintViolation<RegistroPacienteRequest>> violations = validator.validate(r);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("numeroDocumento")));
    }

    @Test
    @DisplayName("Documento vacío debe fallar la validación")
    void documento_vacio_debeViolacion() {
        RegistroPacienteRequest r = requestValido();
        r.setNumeroDocumento("");

        Set<ConstraintViolation<RegistroPacienteRequest>> violations = validator.validate(r);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Nombres con números debe fallar la validación")
    void nombres_conNumeros_debeViolacion() {
        RegistroPacienteRequest r = requestValido();
        r.setNombres("Juan123");

        Set<ConstraintViolation<RegistroPacienteRequest>> violations = validator.validate(r);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("nombres")));
    }

    @Test
    @DisplayName("Nombres vacíos debe fallar la validación")
    void nombres_vacio_debeViolacion() {
        RegistroPacienteRequest r = requestValido();
        r.setNombres("");

        Set<ConstraintViolation<RegistroPacienteRequest>> violations = validator.validate(r);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Nombres con caracteres especiales debe fallar la validación")
    void nombres_conCaracteresEspeciales_debeViolacion() {
        RegistroPacienteRequest r = requestValido();
        r.setNombres("Juan@Carlos");

        Set<ConstraintViolation<RegistroPacienteRequest>> violations = validator.validate(r);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("nombres")));
    }

    @Test
    @DisplayName("Apellidos con números debe fallar la validación")
    void apellidos_conNumeros_debeViolacion() {
        RegistroPacienteRequest r = requestValido();
        r.setApellidos("Perez123");

        Set<ConstraintViolation<RegistroPacienteRequest>> violations = validator.validate(r);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("apellidos")));
    }


    @Test
    @DisplayName("Fecha de nacimiento futura debe fallar la validación")
    void fechaNacimiento_futura_debeViolacion() {
        RegistroPacienteRequest r = requestValido();
        r.setFechaNacimiento(LocalDate.now().plusYears(1));

        Set<ConstraintViolation<RegistroPacienteRequest>> violations = validator.validate(r);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("fechaNacimiento")));
    }

    @Test
    @DisplayName("Fecha de nacimiento de hoy debe fallar la validación (@Past estricto)")
    void fechaNacimiento_hoy_debeViolacion() {
        RegistroPacienteRequest r = requestValido();
        r.setFechaNacimiento(LocalDate.now());

        Set<ConstraintViolation<RegistroPacienteRequest>> violations = validator.validate(r);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("fechaNacimiento")));
    }

    @Test
    @DisplayName("Fecha de nacimiento nula no debe fallar (campo opcional)")
    void fechaNacimiento_nula_noDebeViolacion() {
        RegistroPacienteRequest r = requestValido();
        r.setFechaNacimiento(null);

        Set<ConstraintViolation<RegistroPacienteRequest>> violations = validator.validate(r);

        assertTrue(violations.stream()
                .noneMatch(v -> v.getPropertyPath().toString().equals("fechaNacimiento")));
    }

  
    @Test
    @DisplayName("Correo con formato inválido debe fallar la validación")
    void correo_formatoInvalido_debeViolacion() {
        RegistroPacienteRequest r = requestValido();
        r.setCorreoElectronico("correo-invalido");

        Set<ConstraintViolation<RegistroPacienteRequest>> violations = validator.validate(r);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("correoElectronico")));
    }

    @Test
    @DisplayName("Correo nulo no debe fallar (campo opcional)")
    void correo_nulo_noDebeViolacion() {
        RegistroPacienteRequest r = requestValido();
        r.setCorreoElectronico(null);

        Set<ConstraintViolation<RegistroPacienteRequest>> violations = validator.validate(r);

        assertTrue(violations.stream()
                .noneMatch(v -> v.getPropertyPath().toString().equals("correoElectronico")));
    }


    @Test
    @DisplayName("Username vacío debe fallar la validación")
    void username_vacio_debeViolacion() {
        RegistroPacienteRequest r = requestValido();
        r.setUsername("");

        Set<ConstraintViolation<RegistroPacienteRequest>> violations = validator.validate(r);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("username")));
    }

    @Test
    @DisplayName("Password menor a 6 caracteres debe fallar la validación")
    void password_menosDe6Caracteres_debeViolacion() {
        RegistroPacienteRequest r = requestValido();
        r.setPassword("123");

        Set<ConstraintViolation<RegistroPacienteRequest>> violations = validator.validate(r);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    @DisplayName("Género nulo debe fallar la validación")
    void genero_nulo_debeViolacion() {
        RegistroPacienteRequest r = requestValido();
        r.setGenero(null);

        Set<ConstraintViolation<RegistroPacienteRequest>> violations = validator.validate(r);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("genero")));
    }
}
