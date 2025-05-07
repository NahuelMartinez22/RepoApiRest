package com.martinez.dentist.users.controllers;

import com.martinez.dentist.users.models.UserRole;
import jakarta.validation.constraints.*;

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

    public UserRequestDTO(String username, String password, String email, UserRole role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public Long getProfessionalId() {return professionalId;}
    public void setProfessionalId(Long professionalId) {this.professionalId = professionalId;}
}
