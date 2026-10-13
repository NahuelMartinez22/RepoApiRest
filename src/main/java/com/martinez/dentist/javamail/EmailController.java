package com.martinez.dentist.javamail;

import com.martinez.dentist.appointments.services.AppointmentService;
import com.martinez.dentist.users.models.Token;
import com.martinez.dentist.users.models.User;
import com.martinez.dentist.users.repositories.TokenRepository;
import com.martinez.dentist.users.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private AppointmentService appointmentService;

    private static final String RECOVERY_SENT_MESSAGE =
            "Si el correo está registrado, te enviamos un enlace para cambiar tu contraseña. Revisá tu casilla de email.";

    // Solo envía el enlace de recuperación a usuarios registrados. La respuesta es la misma
    // exista o no el usuario, para no revelar qué correos están registrados.
    @PostMapping("/enviar")
    public ResponseEntity<String> enviarCorreo(@RequestBody PasswordRecoveryRequest request) {
        String email = request.getEmail() == null ? "" : request.getEmail().trim();
        if (email.isEmpty()) {
            return ResponseEntity.badRequest().body("Ingresá un correo electrónico.");
        }

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.ok(RECOVERY_SENT_MESSAGE);
        }

        User user = userOptional.get();

        String tokenStr = UUID.randomUUID().toString();
        LocalDateTime expiration = LocalDateTime.now().plusHours(1);

        Token token = new Token();
        token.setToken(tokenStr);
        token.setUser(user);
        token.setExpirationDate(expiration);
        tokenRepository.save(token);

        String resetLink = "https://odonto-turno.up.railway.app/cambiar-contrasena?token=" + tokenStr;

        EmailDTO dto = new EmailDTO();
        dto.setRecipient(user.getEmail());
        dto.setSubject("Recuperación de contraseña");
        dto.setBody("Hacé clic en el siguiente enlace para cambiar tu contraseña:<br><br>"
                + "<a href=\"" + resetLink + "\">" + resetLink + "</a>");

        emailService.send(dto);

        return ResponseEntity.ok(RECOVERY_SENT_MESSAGE);
    }

    // La contraseña nueva se recibe en el cuerpo del pedido para que no quede en logs ni historial.
    // Los parámetros de URL se aceptan solo por compatibilidad con el frontend anterior.
    @PostMapping("/cambiar-contrasena")
    @Transactional
    public ResponseEntity<String> cambiarPassword(@RequestBody(required = false) ResetPasswordRequest body,
                                                  @RequestParam(value = "token", required = false) String tokenParam,
                                                  @RequestParam(value = "newPassword", required = false) String passwordParam) {

        String tokenValue = body != null && body.getToken() != null ? body.getToken() : tokenParam;
        String newPassword = body != null && body.getNewPassword() != null ? body.getNewPassword() : passwordParam;

        if (tokenValue == null || tokenValue.isBlank() || newPassword == null || newPassword.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Faltan datos para cambiar la contraseña.");
        }

        Optional<Token> tokenOpt = tokenRepository.findByToken(tokenValue);

        if (tokenOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token inválido.");
        }

        Token token = tokenOpt.get();

        if (token.getExpirationDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El token ha expirado.");
        }

        User user = token.getUser();
        user.updatePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        tokenRepository.delete(token);

        return ResponseEntity.ok("Contraseña cambiada correctamente.");
    }

    @PatchMapping("/appointments/confirm/{token}")
    @Transactional
    public ResponseEntity<String> confirmTurno(@PathVariable String token) {
        boolean confirmado = appointmentService.confirmByToken(token);
        if (confirmado) {
            return ResponseEntity.ok("✅ Tu turno ha sido confirmado. ¡Te esperamos!");
        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("❌ No se pudo confirmar el turno. Token inválido o ya confirmado.");
        }
    }

    @PatchMapping("/appointments/cancel/{token}")
    @Transactional
    public ResponseEntity<String> cancelTurno(@PathVariable String token) {
        boolean cancelado = appointmentService.cancelByToken(token);
        if (cancelado) {
            return ResponseEntity.ok("❌ Tu turno ha sido cancelado correctamente.");
        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("❌ No se pudo cancelar el turno. Token inválido o ya cancelado.");
        }
    }
}
