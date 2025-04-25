package com.martinez.dentist.appointments.controllers;

import com.martinez.dentist.patients.controllers.PatientResponseDTO;

import java.time.LocalDateTime;

public class AppointmentResponseDTO {

    private Long id;
    private PatientResponseDTO patient;
    private LocalDateTime dateTime;
    private String professionalFullName;
    private String reason;
    private String state;

    public AppointmentResponseDTO() {}

    public AppointmentResponseDTO(Long id, PatientResponseDTO patient,
                                  LocalDateTime dateTime, String professionalFullName,
                                  String reason, String state) {
        this.id = id;
        this.patient = patient;
        this.dateTime = dateTime;
        this.professionalFullName = professionalFullName;
        this.reason = reason;
        this.state = state;
    }

    public Long getId() { return id; }
    public PatientResponseDTO getPatient() { return patient; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getProfessionalFullName() { return professionalFullName; }
    public String getReason() { return reason; }
    public String getState() { return state; }

    public void setId(Long id) { this.id = id; }
    public void setPatient(PatientResponseDTO patient) { this.patient = patient; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    public void setProfessionalFullName(String professionalFullName) { this.professionalFullName = professionalFullName; }
    public void setReason(String reason) { this.reason = reason; }
    public void setState(String state) { this.state = state; }
}
