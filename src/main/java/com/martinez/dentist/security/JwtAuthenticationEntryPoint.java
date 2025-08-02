package com.martinez.dentist.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // Responde 401 con JSON indicando expiración o credenciales inválidas
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        // Puedes ajustar el campo "error" según el tipo de excepción si lo deseas
        response.getWriter().write("{\"error\":\"TOKEN_EXPIRED\",\"message\":\"Tu sesión ha expirado\"}");
    }
}
