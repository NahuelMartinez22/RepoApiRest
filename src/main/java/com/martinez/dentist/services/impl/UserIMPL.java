package com.martinez.dentist.services.impl;

import com.martinez.dentist.Dto.UserDTO;
import com.martinez.dentist.models.User;
import com.martinez.dentist.repositories.UserRepository;
import com.martinez.dentist.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserIMPL implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String addUser(UserDTO userDTO) {

        User user = new User(
                userDTO.getId(),
                userDTO.getUsername(),
                this.passwordEncoder.encode(userDTO.getPassword())
        );

        userRepository.save(user);

        return user.getUsername();
    }
}
