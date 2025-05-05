package com.martinez.dentist.notifications.controllers;

import com.martinez.dentist.notifications.services.WhatsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/whatsapp")
public class WhatsAppController {

    @Autowired
    private WhatsAppService whatsappService;

    @PostMapping("/enviar")
    public String enviarMensaje(
            @RequestParam String telefono,
            @RequestParam String mensaje
    ) {
        whatsappService.enviarMensaje(telefono, mensaje);
        return "Mensaje enviado a " + telefono;
    }
}
