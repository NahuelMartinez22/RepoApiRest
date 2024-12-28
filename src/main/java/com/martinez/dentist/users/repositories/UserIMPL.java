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
                this.passwordEncoder.encode(userDTO.getPassword())
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
                    return new LoginResponse("Login Success", true);
                } else {
                    return new LoginResponse("Password does not match", false);
                }
            } else {
                return new LoginResponse("User not found", false);
            }
        } catch (Exception e) {
            return new LoginResponse("An error occurred: " + e.getMessage(), false);
        }
    }
}
