package com.martinez.dentist.users.controllers;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Long getProfessionalId() { return professionalId; }
    public void setProfessionalId(Long professionalId) { this.professionalId = professionalId; }

    public String getProfessionalName() { return professionalName; }
    public void setProfessionalName(String professionalName) { this.professionalName = professionalName; }
}
