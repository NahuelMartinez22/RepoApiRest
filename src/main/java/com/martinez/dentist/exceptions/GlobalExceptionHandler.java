package com.martinez.dentist.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ❗ 400  - Sin cambios
    @ExceptionHandler(NoChangesDetectedException.class)
    public ResponseEntity<Map<String, Object>> handleNoChangesDetected(NoChangesDetectedException ex, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", 400);
        response.put("error", ex.getMessage());
        response.put("path", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // ❗ 400 - Errores comunes de lógica
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", 400);
        response.put("error", "Error de validación");
        response.put("validationErrors", fieldErrors);
        response.put("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // ❗ 404 - Recurso no encontrado
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NoSuchElementException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 404,
                "error", "Recurso no encontrado: " + ex.getMessage(),
                "path", request.getRequestURI()
        ));
    }

    // ❗ 403 - Acceso denegado (sin rol adecuado)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 403,
                "error", "No tenés permisos para realizar esta acción.",
                "path", request.getRequestURI()
        ));
    }

    // ❗ 401 - Credenciales inválidas o token ausente
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorized(BadCredentialsException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 401,
                "error", "Credenciales inválidas o token ausente.",
                "path", request.getRequestURI()
        ));
    }

    // ❗ 400 - Datos inválidos (por ejemplo, un rango de fechas al revés)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return businessError(ex.getMessage(), request);
    }

    // ❗ 400 - Reglas de negocio. Los servicios avisan con un RuntimeException y un mensaje para el usuario
    // (por ejemplo, "El profesional ya tiene un turno asignado en ese horario."). Las subclases
    // (NullPointerException, etc.) son errores del sistema y siguen como 500.
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex, HttpServletRequest request) {
        if (ex.getClass() == RuntimeException.class && ex.getMessage() != null && !ex.getMessage().isBlank()) {
            return businessError(ex.getMessage(), request);
        }
        return handleGeneric(ex, request);
    }

    private ResponseEntity<Map<String, Object>> businessError(String message, HttpServletRequest request) {
        String text = message == null || message.isBlank() ? "Los datos enviados no son válidos." : message;
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 400,
                "message", text,
                "error", text,
                "path", request.getRequestURI()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 500,
                "error", "Error inesperado: " + ex.getClass().getSimpleName() + " - " + ex.getMessage(),
                "path", request.getRequestURI()
        ));
    }
}
