package com.martinez.dentist.javamail;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class EmailServiceTest {

    private EmailDTO correo() {
        EmailDTO dto = new EmailDTO();
        dto.setRecipient("usuario@clinica.com");
        dto.setSubject("Recuperación de contraseña");
        dto.setBody("enlace");
        return dto;
    }

    @Test
    void sinConfiguracionNoIntentaEnviarNiLanzaError() {
        EmailService service = new EmailService();
        ReflectionTestUtils.setField(service, "mailHost", "smtp.ejemplo.com");
        ReflectionTestUtils.setField(service, "username", "");
        ReflectionTestUtils.setField(service, "password", "");

        assertThat(service.isConfigured()).isFalse();
        assertThat(service.deliver(correo())).isFalse();
    }

    @Test
    void conCamposVaciosTampocoLanzaError() {
        EmailService service = new EmailService();

        assertThat(service.deliver(correo())).isFalse();
    }

    @Test
    void siElServidorNoRespondeDevuelveFalseSinLanzarError() {
        EmailService service = new EmailService();
        // Puerto cerrado en la máquina local: la conexión falla enseguida
        ReflectionTestUtils.setField(service, "mailHost", "127.0.0.1");
        ReflectionTestUtils.setField(service, "mailPort", "1");
        ReflectionTestUtils.setField(service, "username", "usuario");
        ReflectionTestUtils.setField(service, "password", "clave");
        ReflectionTestUtils.setField(service, "smtpAuth", "true");
        ReflectionTestUtils.setField(service, "smtpEnable", "false");
        ReflectionTestUtils.setField(service, "from", "no-responder@odontoturno.local");

        assertThat(service.isConfigured()).isTrue();
        assertThat(service.deliver(correo())).isFalse();
    }

    // ---------- Resend ----------

    private HttpServer server;
    private final AtomicReference<String> receivedBody = new AtomicReference<>();
    private final AtomicReference<String> receivedAuth = new AtomicReference<>();

    @AfterEach
    void stopServer() {
        if (server != null) {
            server.stop(0);
        }
    }

    // Servidor HTTP local que hace de API de Resend y responde con el código indicado
    private String fakeResend(int status, String response) throws Exception {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/emails", exchange -> {
            receivedAuth.set(exchange.getRequestHeaders().getFirst("Authorization"));
            receivedBody.set(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(status, bytes.length);
            try (OutputStream out = exchange.getResponseBody()) {
                out.write(bytes);
            }
        });
        server.start();
        return "http://127.0.0.1:" + server.getAddress().getPort() + "/emails";
    }

    private EmailService resendService(String url) {
        EmailService service = new EmailService();
        ReflectionTestUtils.setField(service, "resendApiKey", "re_prueba");
        ReflectionTestUtils.setField(service, "resendUrl", url);
        ReflectionTestUtils.setField(service, "from", "no-responder@odontoturno.com");
        return service;
    }

    @Test
    void conClaveDeResendEnviaPorLaApi() throws Exception {
        EmailService service = resendService(fakeResend(200, "{\"id\":\"abc\"}"));

        assertThat(service.isConfigured()).isTrue();
        assertThat(service.deliver(correo())).isTrue();

        assertThat(receivedAuth.get()).isEqualTo("Bearer re_prueba");
        JsonNode body = new ObjectMapper().readTree(receivedBody.get());
        assertThat(body.get("from").asText()).isEqualTo("OdontoTurno <no-responder@odontoturno.com>");
        assertThat(body.get("to").get(0).asText()).isEqualTo("usuario@clinica.com");
        assertThat(body.get("subject").asText()).isEqualTo("Recuperación de contraseña");
        assertThat(body.get("html").asText()).isEqualTo("enlace");
    }

    @Test
    void siResendRechazaElCorreoDevuelveFalse() throws Exception {
        EmailService service = resendService(fakeResend(422, "{\"message\":\"dominio no verificado\"}"));

        assertThat(service.deliver(correo())).isFalse();
    }

    @Test
    void conResendElRemitenteEsObligatorio() throws Exception {
        EmailService service = resendService(fakeResend(200, "{}"));
        ReflectionTestUtils.setField(service, "from", "");

        assertThat(service.deliver(correo())).isFalse();
        assertThat(receivedBody.get()).isNull();
    }
}
