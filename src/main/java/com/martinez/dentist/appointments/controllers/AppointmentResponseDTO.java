package com.martinez.dentist.appointments.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.martinez.dentist.pacients.controllers.PatientResponseDTO;
import com.martinez.dentist.pacients.models.Patient;

import java.time.LocalDateTime;

public class AppointmentResponseDTO {

    private Number appointmentId;
    private String patientDni;
    private String patientFullName;
    private LocalDateTime dateTime;
    private String professionalFullName;
    private String professionalDni;
    private String reason;
    private String state;
    private Patient patient;

    public AppointmentResponseDTO() {}

    public AppointmentResponseDTO(Number appointmentId, String patientDni, String patientFullName,
                                  LocalDateTime dateTime, String professionalFullName, String professionalDni,
                                  String reason, String state) {
        this.appointmentId = appointmentId;
        this.patientDni = patientDni;
        this.professionalDni = professionalDni;
        this.patientFullName = patientFullName;
        this.dateTime = dateTime;
        this.professionalFullName = professionalFullName;
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

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getProfessionalDni() {
        return professionalDni;
    }

    public void setProfessionalDni(String professionalDni) {
        this.professionalDni = professionalDni;
    }
}
