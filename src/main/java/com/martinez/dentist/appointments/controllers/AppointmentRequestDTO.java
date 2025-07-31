package com.martinez.dentist.appointments.controllers;

import com.martinez.dentist.appointments.models.AppointmentState;

import java.time.LocalDateTime;

public class AppointmentRequestDTO {

    private String patientDni;
    private LocalDateTime dateTime;
    private Long professionalId;
    private String reason;
    private String note;
    private AppointmentState state;

    public AppointmentRequestDTO() {}

    public AppointmentRequestDTO(String patientDni, LocalDateTime dateTime,
                                 Long professionalId, String reason,
                                 String note, AppointmentState state) {
        this.patientDni = patientDni;
        this.dateTime = dateTime;
        this.professionalId = professionalId;
        this.reason = reason;
        this.note = note;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public AppointmentState getState() {
        return state;
    }

    public void setState(AppointmentState state) {
        this.state = state;
    }
}
