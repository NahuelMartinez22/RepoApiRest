package com.martinez.dentist.patients.controllers;

import java.time.LocalDate;

public class PatientResponseDTO {

    private Long id;
    private String fullName;
    private String documentType;
    private String documentNumber;

    private Long healthInsuranceId;
    private String healthInsuranceName;

    private Long insurancePlanId;
    private String insurancePlanName;

    private String affiliateNumber;
    private String phone;
    private String email;

    private LocalDate registrationDate;
    private LocalDate lastVisitDate;

    private String note;
    private String state;

    public PatientResponseDTO(Long id, String fullName, String documentType, String documentNumber,
                              Long healthInsuranceId, String healthInsuranceName,
                              Long insurancePlanId, String insurancePlanName,
                              String affiliateNumber, String phone, String email,
                              LocalDate registrationDate, LocalDate lastVisitDate,
                              String note, String state) {
        this.id = id;
        this.fullName = fullName;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.healthInsuranceId = healthInsuranceId;
        this.healthInsuranceName = healthInsuranceName;
        this.insurancePlanId = insurancePlanId;
        this.insurancePlanName = insurancePlanName;
        this.affiliateNumber = affiliateNumber;
        this.phone = phone;
        this.email = email;
        this.registrationDate = registrationDate;
        this.lastVisitDate = lastVisitDate;
        this.note = note;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public Long getHealthInsuranceId() {
        return healthInsuranceId;
    }

    public String getHealthInsuranceName() {
        return healthInsuranceName;
    }

    public Long getInsurancePlanId() {
        return insurancePlanId;
    }

    public String getInsurancePlanName() {
        return insurancePlanName;
    }

    public String getAffiliateNumber() {
        return affiliateNumber;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public LocalDate getLastVisitDate() {
        return lastVisitDate;
    }

    public String getNote() {
        return note;
    }

    public String getState() {
        return state;
    }
}
