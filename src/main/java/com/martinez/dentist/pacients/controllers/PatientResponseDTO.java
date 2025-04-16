package com.martinez.dentist.pacients.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class PatientResponseDTO {

    private Long id;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("document_type")
    private String documentType;

    @JsonProperty("document_number")
    private String documentNumber;

    @JsonProperty("health_insurance")
    private String healthInsurance;

    @JsonProperty("insurance_plan")
    private String insurancePlan;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("registration_date")
    private LocalDate registrationDate;

    @JsonProperty("last_visit_date")
    private LocalDate lastVisitDate;

    @JsonProperty("note")
    private String note;

    public PatientResponseDTO() {
    }

    public PatientResponseDTO(Long id, String fullName, String documentType, String documentNumber,
                              String healthInsurance, String insurancePlan, String phone,
                              LocalDate registrationDate, LocalDate lastVisitDate, String note) {
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
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getHealthInsurance() {
        return healthInsurance;
    }

    public void setHealthInsurance(String healthInsurance) {
        this.healthInsurance = healthInsurance;
    }

    public String getInsurancePlan() {
        return insurancePlan;
    }

    public void setInsurancePlan(String insurancePlan) {
        this.insurancePlan = insurancePlan;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDate getLastVisitDate() {
        return lastVisitDate;
    }

    public void setLastVisitDate(LocalDate lastVisitDate) {
        this.lastVisitDate = lastVisitDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
