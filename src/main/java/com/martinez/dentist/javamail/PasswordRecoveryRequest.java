package com.martinez.dentist.javamail;

import com.fasterxml.jackson.annotation.JsonAlias;

public class PasswordRecoveryRequest {

    // "destinatario" es el nombre que usaba el frontend anterior
    @JsonAlias("destinatario")
    private String email;

    public PasswordRecoveryRequest() {}

    public PasswordRecoveryRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
