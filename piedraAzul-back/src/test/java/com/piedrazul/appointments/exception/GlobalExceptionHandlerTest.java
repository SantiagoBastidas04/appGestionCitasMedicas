package com.piedrazul.appointments.exception;

import com.piedrazul.appointments.shared.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Tests del GlobalExceptionHandler")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler  = new GlobalExceptionHandler();
        request  = new MockHttpServletRequest();
        request.setRequestURI("/api/auth/registro");
    }


    @Test
    @DisplayName("UsuarioDuplicadoException debe retornar 409 con mensaje claro")
    void handleUsuarioDuplicado_debeRetornar409() {
        UsuarioDuplicadoException ex = new UsuarioDuplicadoException("juanito");

        ResponseEntity<Map<String, Object>> response =
                handler.handleUsuarioDuplicado(ex, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(409, response.getBody().get("status"));
        assertEquals("Usuario duplicado", response.getBody().get("error"));
        assertTrue(response.getBody().get("mensaje").toString().contains("juanito"));
        assertEquals("/api/auth/registro", response.getBody().get("path"));
    }

    @Test
    @DisplayName("UsuarioDuplicadoException debe incluir timestamp en la respuesta")
    void handleUsuarioDuplicado_debeIncluirTimestamp() {
        UsuarioDuplicadoException ex = new UsuarioDuplicadoException("test");

        ResponseEntity<Map<String, Object>> response =
                handler.handleUsuarioDuplicado(ex, request);

        assertNotNull(response.getBody().get("timestamp"));
    }

 
    @Test
    @DisplayName("DocumentoDuplicadoException debe retornar 409 con mensaje del documento")
    void handleDocumentoDuplicado_debeRetornar409() {
        DocumentoDuplicadoException ex = new DocumentoDuplicadoException("1061234567");

        ResponseEntity<Map<String, Object>> response =
                handler.handleDocumentoDuplicado(ex, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(409, response.getBody().get("status"));
        assertEquals("Documento duplicado", response.getBody().get("error"));
        assertTrue(response.getBody().get("mensaje").toString().contains("1061234567"));
    }

    
    @Test
    @DisplayName("CorreoDuplicadoException debe retornar 409 con mensaje del correo")
    void handleCorreoDuplicado_debeRetornar409() {
        CorreoDuplicadoException ex = new CorreoDuplicadoException("ana@mail.com");

        ResponseEntity<Map<String, Object>> response =
            handler.handleCorreo(ex, request);
        
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(409, response.getBody().get("status"));
        assertTrue(response.getBody().get("mensaje").toString().contains("ana@mail.com"));
    }

    
    @Test
    @DisplayName("RegistroException debe retornar 500 con mensaje de error")
    void handleRegistro_debeRetornar500() {
        RegistroException ex = new RegistroException("No se pudo completar el registro.");

        ResponseEntity<Map<String, Object>> response =
                handler.handleRegistro(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(500, response.getBody().get("status"));
        assertEquals("Error en el registro", response.getBody().get("error"));
        assertEquals("No se pudo completar el registro.", response.getBody().get("mensaje"));
    }


    @Test
    @DisplayName("MethodArgumentNotValidException debe retornar 400 con lista de campos")
    void handleValidacion_debeRetornar400ConCampos() {
        FieldError fieldError = new FieldError(
                "registroPacienteRequest", "celular",
                "El celular debe contener exactamente 10 dígitos"
        );

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Map<String, Object>> response =
                handler.handleValidacion(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("Datos inválidos", response.getBody().get("error"));
        assertNotNull(response.getBody().get("campos"));

        @SuppressWarnings("unchecked")
        List<Map<String, String>> campos =
                (List<Map<String, String>>) response.getBody().get("campos");

        assertEquals(1, campos.size());
        assertEquals("celular", campos.get(0).get("campo"));
        assertTrue(campos.get(0).get("mensaje").contains("10 dígitos"));
    }

    @Test
    @DisplayName("handleValidacion debe incluir el path del request en la respuesta")
    void handleValidacion_debeIncluirPath() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of());

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Map<String, Object>> response =
                handler.handleValidacion(ex, request);

        assertEquals("/api/auth/registro", response.getBody().get("path"));
    }

    
    @Test
    @DisplayName("UsuarioDuplicadoException debe construir mensaje con el username")
    void usuarioDuplicadoException_debeConstruirMensajeCorrecto() {
        UsuarioDuplicadoException ex = new UsuarioDuplicadoException("pepito");
        assertTrue(ex.getMessage().contains("pepito"));
    }

    @Test
    @DisplayName("DocumentoDuplicadoException debe construir mensaje con el documento")
    void documentoDuplicadoException_debeConstruirMensajeCorrecto() {
        DocumentoDuplicadoException ex = new DocumentoDuplicadoException("1234567890");
        assertTrue(ex.getMessage().contains("1234567890"));
    }

    @Test
    @DisplayName("CorreoDuplicadoException debe construir mensaje con el correo")
    void correoDuplicadoException_debeConstruirMensajeCorrecto() {
        CorreoDuplicadoException ex = new CorreoDuplicadoException("test@mail.com");
        assertTrue(ex.getMessage().contains("test@mail.com"));
    }

    @Test
    @DisplayName("RegistroException debe construir mensaje con el texto dado")
    void registroException_debeConstruirMensajeCorrecto() {
        RegistroException ex = new RegistroException("Error crítico");
        assertEquals("Error crítico", ex.getMessage());
    }
}
