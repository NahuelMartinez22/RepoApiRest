package com.martinez.dentist.professionals.controllers;

import java.time.LocalTime;

public class ProfessionalResponseDTO {

    private Long id;
    private String fullName;
    private String documentType;
    private String documentNumber;
    private String phone;
    private LocalTime startTime;
    private LocalTime endTime;
    private String state;

    public ProfessionalResponseDTO() {}

    public ProfessionalResponseDTO(Long id, String fullName, String documentType,
                                   String documentNumber, String phone,
                                   LocalTime startTime, LocalTime endTime,
                                   String state) {
        this.id = id;
        this.fullName = fullName;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.phone = phone;
        this.startTime = startTime;
        this.endTime = endTime;
        this.state = state;
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getDocumentType() { return documentType; }
    public String getDocumentNumber() { return documentNumber; }
    public String getPhone() { return phone; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public String getState() { return state; }

    public void setId(Long id) { this.id = id; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }
    public void setDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public void setState(String state) { this.state = state; }
}
