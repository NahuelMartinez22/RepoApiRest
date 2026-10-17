package com.martinez.dentist.javamail;

import jakarta.annotation.PreDestroy;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Envía los correos en segundo plano: el pedido HTTP responde enseguida aunque el servidor
// de correo tarde o no responda. Si el envío falla, queda registrado en el log.
//
// Hay dos formas de envío, en este orden:
// 1. Resend, por su API HTTPS, si está cargada RESEND_API_KEY (producción: Railway bloquea SMTP en el plan Hobby).
// 2. SMTP, si están cargados host, usuario y contraseña (por ejemplo, Mailtrap en local).
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private static final String UTF_8 = "UTF-8";
    private static final String SENDER_NAME = "OdontoTurno";
    // Tiempo máximo para conectar, leer y escribir contra el servidor de correo
    static final String SMTP_TIMEOUT_MS = "10000";
    static final Duration HTTP_TIMEOUT = Duration.ofSeconds(10);
    static final String RESEND_URL = "https://api.resend.com/emails";

    @Value("${app.mail.resend-api-key:}")
    private String resendApiKey;

    // Se puede cambiar solo para los tests
    private String resendUrl = RESEND_URL;

    @Value("${spring.mail.host:}")
    private String mailHost;

    @Value("${spring.mail.port:}")
    private String mailPort;

    @Value("${spring.mail.username:}")
    private String username;

    @Value("${spring.mail.password:}")
    private String password;

    @Value("${spring.mail.properties.mail.smtp.auth:true}")
    private String smtpAuth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable:true}")
    private String smtpEnable;

    // Remitente, por ejemplo no-responder@odontoturno.com. Con Resend es obligatorio y tiene que ser
    // del dominio verificado. Con SMTP, si no se indica, se usa el usuario SMTP.
    @Value("${app.mail.from:}")
    private String from;

    // Solo para desarrollo: si el correo no está configurado o falla, muestra el mensaje
    // (con el enlace de recuperación) en la consola para poder seguir probando.
    @Value("${app.mail.log-body-on-failure:false}")
    private boolean logBodyOnFailure;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newBuilder().connectTimeout(HTTP_TIMEOUT).build();

    private final ExecutorService executor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable, "email-sender");
        thread.setDaemon(true);
        return thread;
    });

    public void send(EmailDTO email) {
        executor.submit(() -> deliver(email));
    }

    // Envía el correo y devuelve si salió. No lanza excepciones.
    boolean deliver(EmailDTO email) {
        if (usesResend()) {
            return deliverWithResend(email);
        }
        if (smtpConfigured()) {
            return deliverWithSmtp(email);
        }
        log.warn("Correo no configurado (falta RESEND_API_KEY o los datos de SMTP): no se envió \"{}\" a {}",
                email.getSubject(), email.getRecipient());
        logBodyIfAllowed(email);
        return false;
    }

    private boolean deliverWithResend(EmailDTO email) {
        if (!notBlank(from)) {
            log.error("Falta el remitente (MAIL_FROM) para enviar con Resend: no se envió \"{}\" a {}",
                    email.getSubject(), email.getRecipient());
            logBodyIfAllowed(email);
            return false;
        }
        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(resendUrl))
                    .timeout(HTTP_TIMEOUT)
                    .header("Authorization", "Bearer " + resendApiKey.trim())
                    .header("Content-Type", "application/json")
                    // Evita correos duplicados si el pedido se reintenta
                    .header("Idempotency-Key", UUID.randomUUID().toString())
                    .POST(HttpRequest.BodyPublishers.ofString(resendBody(email), StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() / 100 == 2) {
                log.info("Correo \"{}\" enviado a {} con Resend", email.getSubject(), email.getRecipient());
                return true;
            }
            log.error("Resend rechazó el correo \"{}\" a {} (HTTP {}): {}",
                    email.getSubject(), email.getRecipient(), response.statusCode(), response.body());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Se interrumpió el envío del correo \"{}\" a {}", email.getSubject(), email.getRecipient());
        } catch (Exception e) {
            log.error("No se pudo enviar el correo \"{}\" a {} con Resend: {}",
                    email.getSubject(), email.getRecipient(), e.getMessage());
        }
        logBodyIfAllowed(email);
        return false;
    }

    // Cuerpo JSON del pedido a la API de Resend
    String resendBody(EmailDTO email) throws Exception {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("from", senderWithName());
        body.put("to", List.of(email.getRecipient()));
        body.put("subject", email.getSubject());
        body.put("html", email.getBody());
        return objectMapper.writeValueAsString(body);
    }

    private boolean deliverWithSmtp(EmailDTO email) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", mailHost);
            props.put("mail.smtp.port", valueOr(mailPort, "587"));
            props.put("mail.smtp.auth", valueOr(smtpAuth, "true"));
            props.put("mail.smtp.starttls.enable", valueOr(smtpEnable, "true"));
            // Sin estos valores, JavaMail espera para siempre si el puerto SMTP está bloqueado
            props.put("mail.smtp.connectiontimeout", SMTP_TIMEOUT_MS);
            props.put("mail.smtp.timeout", SMTP_TIMEOUT_MS);
            props.put("mail.smtp.writetimeout", SMTP_TIMEOUT_MS);

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender(), SENDER_NAME, UTF_8));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getRecipient()));
            message.setSubject(email.getSubject(), UTF_8);
            message.setContent(email.getBody(), "text/html; charset=UTF-8");

            Transport.send(message);
            log.info("Correo \"{}\" enviado a {}", email.getSubject(), email.getRecipient());
            return true;

        } catch (Exception e) {
            log.error("No se pudo enviar el correo \"{}\" a {} por {}:{}: {}",
                    email.getSubject(), email.getRecipient(), mailHost, mailPort, e.getMessage());
            logBodyIfAllowed(email);
            return false;
        }
    }

    boolean isConfigured() {
        return usesResend() || smtpConfigured();
    }

    boolean usesResend() {
        return notBlank(resendApiKey);
    }

    private boolean smtpConfigured() {
        return notBlank(mailHost) && notBlank(username) && notBlank(password);
    }

    // "OdontoTurno <no-responder@odontoturno.com>"; si ya viene con nombre, se deja igual
    private String senderWithName() {
        String address = from.trim();
        return address.contains("<") ? address : SENDER_NAME + " <" + address + ">";
    }
    private String sender() {
        return notBlank(from) ? from.trim() : username;
    }

    private void logBodyIfAllowed(EmailDTO email) {
        if (logBodyOnFailure) {
            log.warn("[solo desarrollo] Contenido del correo para {}: {}", email.getRecipient(), email.getBody());
        }
    }

    private static String valueOr(String value, String fallback) {
        return notBlank(value) ? value.trim() : fallback;
    }

    private static boolean notBlank(String value) {
        return value != null && !value.isBlank();
    }

    @PreDestroy
    void shutdown() {
        executor.shutdown();
    }
}
