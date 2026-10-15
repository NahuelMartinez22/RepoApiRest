package com.martinez.dentist.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();
    private final MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/appointments");

    @Test
    void lasReglasDeNegocioDevuelven400ConElMensaje() {
        ResponseEntity<Map<String, Object>> response = handler.handleRuntime(
                new RuntimeException("El profesional ya tiene un turno asignado en ese horario."), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("message", "El profesional ya tiene un turno asignado en ese horario.");
        assertThat(response.getBody()).containsEntry("error", "El profesional ya tiene un turno asignado en ese horario.");
    }

    @Test
    void losErroresDelSistemaSiguenSiendo500() {
        ResponseEntity<Map<String, Object>> response = handler.handleRuntime(new NullPointerException("x"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void losDatosInvalidosDevuelven400() {
        ResponseEntity<Map<String, Object>> response = handler.handleIllegalArgument(
                new IllegalArgumentException("El rango máximo es de dos meses."), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("message", "El rango máximo es de dos meses.");
    }
}
