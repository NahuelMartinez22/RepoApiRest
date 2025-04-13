package com.martinez.dentist.javamail;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
public class EmailService {
    public static void enviar(EmailDTO email) throws MessagingException {
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

        Message mensaje = new MimeMessage(session);
        mensaje.setFrom(new InternetAddress(remitente));
        mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.destinatario));
        mensaje.setSubject(email.asunto);
        mensaje.setText(email.cuerpo);

        Transport.send(mensaje);
    }
}
