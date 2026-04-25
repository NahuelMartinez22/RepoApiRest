package com.martinez.dentist.users.controllers;

import lombok.Getter;
import lombok.Setter;

@Getter
public class LoginResponseDTO {

    private String username;
    private String email;
    private String role;
    @Setter
    private Long professionalId;
    private String professionalName;

    public LoginResponseDTO(String username, String email, String role,
                            Long professionalId, String professionalName) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.professionalId = professionalId;
        this.professionalName = professionalName;
    }
}
