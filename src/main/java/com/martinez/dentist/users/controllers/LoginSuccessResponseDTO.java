package com.martinez.dentist.users.controllers;

import lombok.Getter;

@Getter
public class LoginSuccessResponseDTO {
    private String token;
    private UserResponseDTO user;

    public LoginSuccessResponseDTO(String token, UserResponseDTO user) {
        this.token = token;
        this.user = user;
    }

}
