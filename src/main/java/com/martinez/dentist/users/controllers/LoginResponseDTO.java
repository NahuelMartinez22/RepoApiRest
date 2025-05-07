package com.martinez.dentist.users.controllers;

public class LoginResponseDTO {

    private String username;
    private String email;
    private String role;
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

    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public Long getProfessionalId() { return professionalId; }
    public String getProfessionalName() { return professionalName; }

    public void setProfessionalId(Long professionalId) { this.professionalId = professionalId; }
    public void setProfessionalName(String professionalName) { this.professionalName = professionalName; }
}
