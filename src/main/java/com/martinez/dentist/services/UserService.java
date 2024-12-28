package com.martinez.dentist.services;

import com.martinez.dentist.Dto.LoginDTO;
import com.martinez.dentist.Dto.UserDTO;
import com.martinez.dentist.responses.LoginResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    String addUser(UserDTO userDTO);

    LoginResponse loginUser(LoginDTO loginDTO);
}
