package com.piedrazul.appointments.shared.exception;

import com.piedrazul.appointments.shared.security.KeycloakAdminService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.time.LocalDateTime;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidacion(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<Map<String, String>> campos = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            Map<String, String> campo = new HashMap<>();
            campo.put("campo", error.getField());
            campo.put("mensaje", error.getDefaultMessage());
            campos.add(campo);
        });

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("timestamp", LocalDateTime.now().toString());
        respuesta.put("status", 400);
        respuesta.put("error", "Datos inválidos");
        respuesta.put("mensaje", "Uno o más campos no pasaron la validación");
        respuesta.put("path", request.getRequestURI());
        respuesta.put("campos", campos);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    @ExceptionHandler(KeycloakValidationException.class)
    public ResponseEntity<Map<String, Object>> handleKeycloakValidation(
            KeycloakValidationException ex,
            HttpServletRequest request) {

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("timestamp", LocalDateTime.now().toString());
        respuesta.put("status", 400);
        respuesta.put("error", "Datos inválidos");
        respuesta.put("mensaje", ex.getMessage());
        respuesta.put("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    @ExceptionHandler(KeycloakAdminService.KeycloakBadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleKeycloakBadRequest(
            KeycloakAdminService.KeycloakBadRequestException ex,
            HttpServletRequest request) {

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("timestamp", LocalDateTime.now().toString());
        respuesta.put("status", 400);
        respuesta.put("error", "Datos inválidos");
        respuesta.put("mensaje", ex.getMessage());
        respuesta.put("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    @ExceptionHandler(KeycloakAdminService.KeycloakConflictException.class)
    public ResponseEntity<Map<String, Object>> handleKeycloakConflict(
            KeycloakAdminService.KeycloakConflictException ex,
            HttpServletRequest request) {

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("timestamp", LocalDateTime.now().toString());
        respuesta.put("status", 409);
        respuesta.put("error", "Usuario duplicado");
        respuesta.put("mensaje", ex.getMessage());
        respuesta.put("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
    }

    @ExceptionHandler(KeycloakAdminService.KeycloakServiceException.class)
    public ResponseEntity<Map<String, Object>> handleKeycloakService(
            KeycloakAdminService.KeycloakServiceException ex,
            HttpServletRequest request) {

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("timestamp", LocalDateTime.now().toString());
        respuesta.put("status", 500);
        respuesta.put("error", "Error en el registro");
        respuesta.put("mensaje", ex.getMessage());
        respuesta.put("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
    }

    @ExceptionHandler(UsuarioDuplicadoException.class)
    public ResponseEntity<Map<String, Object>> handleUsuarioDuplicado(
            UsuarioDuplicadoException ex,
            HttpServletRequest request) {

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("timestamp", LocalDateTime.now().toString());
        respuesta.put("status", 409);
        respuesta.put("error", "Usuario duplicado");
        respuesta.put("mensaje", ex.getMessage());
        respuesta.put("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
    }

    @ExceptionHandler(DocumentoDuplicadoException.class)
    public ResponseEntity<Map<String, Object>> handleDocumentoDuplicado(
            DocumentoDuplicadoException ex,
            HttpServletRequest request) {

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("timestamp", LocalDateTime.now().toString());
        respuesta.put("status", 409);
        respuesta.put("error", "Documento duplicado");
        respuesta.put("mensaje", ex.getMessage());
        respuesta.put("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
    }

    @ExceptionHandler(RegistroException.class)
    public ResponseEntity<Map<String, Object>> handleRegistro(
            RegistroException ex,
            HttpServletRequest request) {

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("timestamp", LocalDateTime.now().toString());
        respuesta.put("status", 500);
        respuesta.put("error", "Error en el registro");
        respuesta.put("mensaje", ex.getMessage());
        respuesta.put("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
    }


    @ExceptionHandler(CorreoDuplicadoException.class)
    public ResponseEntity<Map<String, Object>> handleCorreo(
            CorreoDuplicadoException ex,
            HttpServletRequest request) {
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("timestamp", LocalDateTime.now().toString());
        respuesta.put("status", 409);
        respuesta.put("error", "Correo duplicado");
        respuesta.put("mensaje", ex.getMessage());
        respuesta.put("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
    }
}