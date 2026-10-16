package com.martinez.dentist.javamail;

import jakarta.annotation.PreDestroy;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Envía los correos en segundo plano: el pedido HTTP responde enseguida aunque el servidor
// de correo tarde o no responda. Si el envío falla, queda registrado en el log.
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private static final String UTF_8 = "UTF-8";
    // Tiempo máximo para conectar, leer y escribir contra el servidor SMTP
    static final String SMTP_TIMEOUT_MS = "10000";

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

    // Remitente. Si no se indica, se usa el usuario SMTP (por ejemplo, la cuenta de Gmail)
    @Value("${app.mail.from:}")
    private String from;

    // Solo para desarrollo: si el correo no está configurado o falla, muestra el mensaje
    // (con el enlace de recuperación) en la consola para poder seguir probando.
    @Value("${app.mail.log-body-on-failure:false}")
    private boolean logBodyOnFailure;

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
        if (!isConfigured()) {
            log.warn("Correo no configurado (faltan spring.mail.host, username o password): no se envió \"{}\" a {}",
                    email.getSubject(), email.getRecipient());
            logBodyIfAllowed(email);
            return false;
        }

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
            message.setFrom(new InternetAddress(sender(), "OdontoTurno", UTF_8));
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
        return notBlank(mailHost) && notBlank(username) && notBlank(password);
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
