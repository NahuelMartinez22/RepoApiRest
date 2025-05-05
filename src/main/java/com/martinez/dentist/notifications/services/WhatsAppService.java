package com.martinez.dentist.notifications.services;

import com.martinez.dentist.config.ConfigService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WhatsAppService {

    private String accountSid;
    private String authToken;
    private final String FROM_NUMBER = "whatsapp:+14155238886";

    @Autowired
    private ConfigService configService;

    @PostConstruct
    public void init() {
        this.accountSid = configService.getValue("twilio_account_sid");
        this.authToken = configService.getValue("twilio_auth_token");

        Twilio.init(accountSid, authToken);
        System.out.println("🔐 Twilio inicializado con credenciales desde la base de datos.");
    }

    public void enviarMensaje(String telefonoDestino, String mensaje) {
        if (!telefonoDestino.startsWith("+")) {
            telefonoDestino = "+" + telefonoDestino;
        }

        Message message = Message.creator(
                new PhoneNumber("whatsapp:" + telefonoDestino),
                new PhoneNumber(FROM_NUMBER),
                mensaje
        ).create();

        System.out.println("✅ Mensaje enviado. SID: " + message.getSid());
    }
}
