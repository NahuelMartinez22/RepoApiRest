package com.martinez.dentist.pacients.controllers;

import java.time.LocalDate;

public class PatientResponseDTO {

    private Long id;
    private String fullName;
    private String documentType;
    private String documentNumber;
    private String healthInsurance;
    private String insurancePlan;
    private String phone;
    private LocalDate registrationDate;
    private LocalDate lastVisitDate;
    private String note;
    private String state;

    public PatientResponseDTO(Long id, String fullName, String documentType, String documentNumber,
                              String healthInsurance, String insurancePlan, String phone,
                              LocalDate registrationDate, LocalDate lastVisitDate, String note, String state) {
        this.id = id;
        this.fullName = fullName;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.healthInsurance = healthInsurance;
        this.insurancePlan = insurancePlan;
        this.phone = phone;
        this.registrationDate = registrationDate;
        this.lastVisitDate = lastVisitDate;
        this.note = note;
        this.state = state;
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getDocumentType() { return documentType; }
    public String getDocumentNumber() { return documentNumber; }
    public String getHealthInsurance() { return healthInsurance; }
    public String getInsurancePlan() { return insurancePlan; }
    public String getPhone() { return phone; }
    public LocalDate getRegistrationDate() { return registrationDate; }
    public LocalDate getLastVisitDate() { return lastVisitDate; }
    public String getNote() { return note; }
    public String getState() { return state; }
}
