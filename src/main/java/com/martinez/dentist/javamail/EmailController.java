package com.martinez.dentist.javamail;

import com.martinez.dentist.users.models.Token;
import com.martinez.dentist.users.models.User;
import com.martinez.dentist.users.repositories.TokenRepository;
import com.martinez.dentist.users.repositories.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class EmailController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping("/enviar")
    public ResponseEntity<String> enviarCorreo(@RequestBody EmailDTO email) throws MessagingException, UnsupportedEncodingException {
        User user = userRepository.findByEmail(email.getDestinatario());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No existe un usuario con ese correo electrónico.");
        }

        String tokenStr = UUID.randomUUID().toString();
        LocalDateTime expiration = LocalDateTime.now().plusHours(1);

        Token token = new Token();
        token.setToken(tokenStr);
        token.setUser(user);
        token.setExpirationDate(expiration);
        tokenRepository.save(token);

        //String resetLink = "http://localhost:8080/api/cambiar-contrasena?token=" + tokenStr;
        String resetLink = "http://localhost:3000/cambiar-contrasena?token=" + tokenStr;

        EmailDTO dto = new EmailDTO();
        dto.setDestinatario(user.getEmail());
        dto.setAsunto("Recuperación de contraseña");
        dto.setCuerpo("Hacé clic en el siguiente enlace para cambiar tu contraseña:\n\n" + resetLink);

        emailService.enviar(dto);

        return ResponseEntity.ok("Te enviamos un correo. Por favor, revisá tu casilla de email.");
    }

    @PostMapping("/cambiar-contrasena")
    @Transactional
    public ResponseEntity<String> cambiarPassword(@RequestParam("token") String tokenParam,
                                                  @RequestParam("newPassword") String newPassword) {

        System.out.println("Token recibido: " + tokenParam);
        Optional<Token> token = tokenRepository.findByToken(tokenParam);

        if (token.isEmpty()) {
            System.out.println("Token no encontrado.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token inválido.");
        }

        if (token.get().getExpirationDate().isBefore(LocalDateTime.now())) {
            System.out.println("Token expirado.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El token ha expirado.");
        }

        User user = token.get().getUser();
        System.out.println("Usuario encontrado: " + user.getEmail());

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        tokenRepository.delete(token.get());

        return ResponseEntity.ok("Contraseña cambiada correctamente.");
    }
}
