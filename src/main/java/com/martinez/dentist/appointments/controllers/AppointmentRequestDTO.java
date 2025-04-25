package com.martinez.dentist.appointments.controllers;

import com.martinez.dentist.appointments.models.AppointmentState;
import com.martinez.dentist.pacients.controllers.PatientResponseDTO;

import java.time.LocalDateTime;

public class AppointmentRequestDTO {

    private Number appointmentId;
    private String patientDni;
    private LocalDateTime dateTime;
    private Long professionalId;
    private String reason;
    private AppointmentState state;
    private PatientResponseDTO patient;


    public AppointmentRequestDTO() {}

    public AppointmentRequestDTO(Number appointmentId, String patientDni, LocalDateTime dateTime,
                                 Long professionalId, String reason, AppointmentState state) {
        this.appointmentId = appointmentId;
        this.patientDni = patientDni;
        this.dateTime = dateTime;
        this.professionalId = professionalId;
        this.reason = reason;
        this.state = state;
    }

    public Number getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Number appointmentId) {
        this.appointmentId = appointmentId;
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

    public PatientResponseDTO getPatient() {
        return patient;
    }

    public void setPatient(PatientResponseDTO patient) {
        this.patient = patient;
    }
}
