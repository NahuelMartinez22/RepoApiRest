package com.martinez.dentist.javamail;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

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
}
