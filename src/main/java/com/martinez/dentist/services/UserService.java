package com.martinez.dentist.services;

import com.martinez.dentist.Dto.UserDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    String addUser(UserDTO userDTO);

}
