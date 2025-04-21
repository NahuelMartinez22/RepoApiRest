package com.martinez.dentist.pacients.controllers;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PatientRequestDTO {

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

    public PatientRequestDTO() {}

    public PatientRequestDTO(String fullName, String documentType, String documentNumber,
                             String healthInsurance, String insurancePlan, String phone,
                             LocalDate registrationDate, LocalDate lastVisitDate, String note) {
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

    @Override
    public String toString() {
        return "PatientRequestDTO{" +
                "fullName='" + fullName + '\'' +
                ", documentType='" + documentType + '\'' +
                ", documentNumber='" + documentNumber + '\'' +
                ", healthInsurance='" + healthInsurance + '\'' +
                ", insurancePlan='" + insurancePlan + '\'' +
                ", phone='" + phone + '\'' +
                ", registrationDate=" + registrationDate +
                ", lastVisitDate=" + lastVisitDate +
                ", note='" + note + '\'' +
                '}';
    }
}
