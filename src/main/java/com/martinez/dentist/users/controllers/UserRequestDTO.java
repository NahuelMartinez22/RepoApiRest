package com.martinez.dentist.users.controllers;

import com.martinez.dentist.users.models.UserRole;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRequestDTO {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    @Email(message = "El email debe ser válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @NotNull(message = "El rol es obligatorio")
    private UserRole role;

    private Long professionalId;

    public UserRequestDTO() {}
}
