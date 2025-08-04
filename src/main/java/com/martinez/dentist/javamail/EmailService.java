package com.martinez.dentist.javamail;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
public class EmailService {
    public static void enviar(EmailDTO email) throws MessagingException, UnsupportedEncodingException {
        String remitente = "nahuel.martinez.243@gmail.com";
        String contraseña = "yjpe jspd halk vmgs";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, contraseña);
            }
        });

        MimeMessage mensaje = new MimeMessage(session);

        mensaje.setFrom(new InternetAddress(remitente, "Tu Clínica", "UTF-8"));

        mensaje.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(email.getDestinatario())
        );

        mensaje.setSubject(email.getAsunto(), "UTF-8");

        mensaje.setHeader("Content-Type", "text/plain; charset=UTF-8");
        mensaje.setHeader("Content-Transfer-Encoding", "8bit");

        mensaje.setText(email.getCuerpo(), "UTF-8");

        Transport.send(mensaje);
    }
}