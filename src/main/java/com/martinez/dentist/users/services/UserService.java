package com.martinez.dentist.users.services;

import com.martinez.dentist.users.controllers.LoginDTO;
import com.martinez.dentist.users.controllers.LoginResponseDTO;
import com.martinez.dentist.users.controllers.UserRequestDTO;
import com.martinez.dentist.users.controllers.UserResponseDTO;
import com.martinez.dentist.users.models.User;

import java.util.List;

public interface UserService {
    String createUser(UserRequestDTO dto);
    List<UserResponseDTO> getAllUsers();
    String updateUser(Long id, UserRequestDTO dto);
    UserResponseDTO getUserById(Long id);
    LoginResponseDTO loginUser(LoginDTO loginDTO);
    String deleteUser(Long id);
}
