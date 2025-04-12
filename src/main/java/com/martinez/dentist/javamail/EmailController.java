package com.martinez.dentist.javamail;

import com.martinez.dentist.users.models.User;
import com.martinez.dentist.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class EmailController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/enviar")
    public ResponseEntity<String> enviarCorreo(@RequestBody EmailDTO email) {

        User user = userRepository.findByEmail(email.getDestinatario());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No existe un usuario con ese correo electrónico.");
        }

        try {
            EmailService.enviar(email);
            return ResponseEntity.ok("Correo enviado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al enviar correo: " + e.getMessage());
        }
    }
}
