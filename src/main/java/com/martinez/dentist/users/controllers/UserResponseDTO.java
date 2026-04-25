package com.martinez.dentist.users.controllers;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponseDTO {

    private Long id;
    private String username;
    private String email;
    private String role;
    private Long professionalId;
    private String professionalName;

    public UserResponseDTO() {}

    public UserResponseDTO(Long id, String username, String email, String role,
                           Long professionalId, String professionalName) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.professionalId = professionalId;
        this.professionalName = professionalName;
    }

}
