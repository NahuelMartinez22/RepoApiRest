package com.martinez.dentist.appointments.controllers;

import java.time.LocalDateTime;

public class AppointmentResponseDTO {

    private String patientDni;
    private String patientFullName;
    private LocalDateTime dateTime;
    private String professionalFullName;
    private String reason;
    private String state;

    public AppointmentResponseDTO() {}

    public AppointmentResponseDTO(String patientDni, String patientFullName,
                                  LocalDateTime dateTime, String professionalFullName,
                                  String reason, String state) {
        this.patientDni = patientDni;
        this.patientFullName = patientFullName;
        this.dateTime = dateTime;
        this.professionalFullName = professionalFullName;
        this.reason = reason;
        this.state = state;
    }

    public String getPatientDni() {
        return patientDni;
    }

    public void setPatientDni(String patientDni) {
        this.patientDni = patientDni;
    }

    public String getPatientFullName() {
        return patientFullName;
    }

    public void setPatientFullName(String patientFullName) {
        this.patientFullName = patientFullName;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getProfessionalFullName() {
        return professionalFullName;
    }

    public void setProfessionalFullName(String professionalFullName) {
        this.professionalFullName = professionalFullName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
