package com.martinez.dentist.appointments.controllers;

public class AttendAppointmentRequestDTO {

    private String credentialToken;

    public AttendAppointmentRequestDTO() {}

    public AttendAppointmentRequestDTO(String credentialToken) {
        this.credentialToken = credentialToken;
    }

    public String getCredentialToken() {
        return credentialToken;
    }

    public void setCredentialToken(String credentialToken) {
        this.credentialToken = credentialToken;
    }
}
