package com.martinez.dentist.users.controllers;

public class LoginSuccessResponseDTO {
    private String token;
    private UserResponseDTO user;

    public LoginSuccessResponseDTO(String token, UserResponseDTO user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public UserResponseDTO getUser() {
        return user;
    }
}
