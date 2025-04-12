package com.martinez.dentist.users.repositories;

import com.martinez.dentist.users.controllers.LoginDTO;
import com.martinez.dentist.users.controllers.UserDTO;
import com.martinez.dentist.users.models.User;
import com.martinez.dentist.users.controllers.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserIMPL {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public String addUser(UserDTO userDTO) {

        User user = new User(
                userDTO.getId(),
                userDTO.getUsername(),
                this.passwordEncoder.encode(userDTO.getPassword()),
                userDTO.getEmail()
        );

        userRepository.save(user);

        return user.getUsername();
    }

    public LoginResponse loginUser(LoginDTO loginDTO) {
        try {
            User user = userRepository.findByUsername(loginDTO.getUsername());
            if (user != null) {
                boolean isPasswordCorrect = passwordEncoder.matches(loginDTO.getPassword(), user.getPassword());
                if (isPasswordCorrect) {
                    return new LoginResponse("Inicio de sesión exitoso", true);
                } else {
                    return new LoginResponse("La contraseña es incorrecta", false);
                }
            } else {
                return new LoginResponse("El usuario no existe", false);
            }
        } catch (Exception e) {
            return new LoginResponse("Ocurrió un error: " + e.getMessage(), false);
        }
    }
}
