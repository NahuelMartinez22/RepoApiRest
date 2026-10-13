package com.martinez.dentist.security;

import com.martinez.dentist.professionals.models.Professional;
import com.martinez.dentist.users.controllers.LoginDTO;
import com.martinez.dentist.users.controllers.LoginSuccessResponseDTO;
import com.martinez.dentist.users.controllers.UserResponseDTO;
import com.martinez.dentist.users.models.User;
import com.martinez.dentist.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        String ip = clientIp(request);

        if (loginAttemptService.isBlocked(loginDTO.getUsername(), ip)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(Map.of(
                    "status", 429,
                    "message", "Demasiados intentos fallidos. Esperá 15 minutos y volvé a intentar."
            ));
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()
                    )
            );
        } catch (AuthenticationException ex) {
            loginAttemptService.loginFailed(loginDTO.getUsername(), ip);
            throw ex;
        }
        loginAttemptService.loginSucceeded(loginDTO.getUsername(), ip);

        User user = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtService.generateToken(user);

        Professional prof = user.getProfessional();
        Long profId = (prof != null) ? prof.getId() : null;
        String profName = (prof != null) ? prof.getFullName() : null;

        UserResponseDTO userDTO = new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                profId,
                profName
        );

        return ResponseEntity.ok(new LoginSuccessResponseDTO(token, userDTO));
    }

    // El proxy de Railway pone la IP real del cliente en X-Real-IP.
    // No se usa X-Forwarded-For porque el cliente puede falsificar su primer valor.
    private String clientIp(HttpServletRequest request) {
        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp.trim();
        }
        return request.getRemoteAddr();
    }

}