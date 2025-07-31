package com.martinez.dentist.appointments.controllers;

import com.martinez.dentist.patients.controllers.PatientResponseDTO;

import java.time.LocalDateTime;

public class AppointmentResponseDTO {

    private Long id;
    private Long patientId;
    private PatientResponseDTO patient;
    private LocalDateTime dateTime;
    private String professionalFullName;
    private String reason;
    private String note;
    private String state;

    public AppointmentResponseDTO() {}

    public AppointmentResponseDTO(Long id, Long patientId, PatientResponseDTO patient,
                                  LocalDateTime dateTime, String professionalFullName,
                                  String reason, String note, String state) {
        this.id = id;
        this.patientId = patientId;
        this.patient = patient;
        this.dateTime = dateTime;
        this.professionalFullName = professionalFullName;
        this.reason = reason;
        this.note = note;
        this.state = state;
    }

    public Long getId() { return id; }
    public Long getPatientId() { return patientId; }
    public PatientResponseDTO getPatient() { return patient; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getProfessionalFullName() { return professionalFullName; }
    public String getReason() { return reason; }
    public String getNote() { return note; }
    public String getState() { return state; }

    public void setId(Long id) { this.id = id; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public void setPatient(PatientResponseDTO patient) { this.patient = patient; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    public void setProfessionalFullName(String professionalFullName) { this.professionalFullName = professionalFullName; }
    public void setReason(String reason) { this.reason = reason; }
    public void setNote(String note) { this.note = note; }
    public void setState(String state) { this.state = state; }
}
