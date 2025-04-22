package com.martinez.dentist.appointments.controllers;

import com.martinez.dentist.appointments.models.AppointmentState;

import java.time.LocalDateTime;

public class AppointmentRequestDTO {

    private String patientDni;
    private LocalDateTime dateTime;
    private Long professionalId;
    private String reason;
    private AppointmentState state;

    public AppointmentRequestDTO() {}

    public AppointmentRequestDTO(String patientDni, LocalDateTime dateTime,
                                 Long professionalId, String reason, AppointmentState state) {
        this.patientDni = patientDni;
        this.dateTime = dateTime;
        this.professionalId = professionalId;
        this.reason = reason;
        this.state = state;
    }

    public String getPatientDni() {
        return patientDni;
    }

    public void setPatientDni(String patientDni) {
        this.patientDni = patientDni;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Long getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(Long professionalId) {
        this.professionalId = professionalId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public AppointmentState getState() {
        return state;
    }

    public void setState(AppointmentState state) {
        this.state = state;
    }
}
