package com.martinez.dentist.security;

import com.martinez.dentist.professionals.models.Professional;
import com.martinez.dentist.users.controllers.LoginDTO;
import com.martinez.dentist.users.controllers.LoginSuccessResponseDTO;
import com.martinez.dentist.users.controllers.UserResponseDTO;
import com.martinez.dentist.users.models.User;
import com.martinez.dentist.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<LoginSuccessResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(),
                        loginDTO.getPassword()
                )
        );

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

}