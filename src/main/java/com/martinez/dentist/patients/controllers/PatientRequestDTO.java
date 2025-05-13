package com.martinez.dentist.patients.controllers;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class PatientRequestDTO {

    @NotBlank(message = "El nombre completo es obligatorio")
    private String fullName;

    @NotBlank(message = "El tipo de documento es obligatorio")
    private String documentType;

    @NotBlank(message = "El número de documento es obligatorio")
    private String documentNumber;

    private Long healthInsuranceId;
    private Long insurancePlanId;
    private String affiliateNumber;

    private String phone;
    private String email;

    private LocalDate registrationDate;
    private LocalDate lastVisitDate;

    private String note;


    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }

    public String getDocumentNumber() { return documentNumber; }
    public void setDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; }

    public Long getHealthInsuranceId() { return healthInsuranceId; }
    public void setHealthInsuranceId(Long healthInsuranceId) { this.healthInsuranceId = healthInsuranceId; }

    public Long getInsurancePlanId() { return insurancePlanId; }
    public void setInsurancePlanId(Long insurancePlanId) { this.insurancePlanId = insurancePlanId; }

    public String getAffiliateNumber() { return affiliateNumber; }
    public void setAffiliateNumber(String affiliateNumber) { this.affiliateNumber = affiliateNumber; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }

    public LocalDate getLastVisitDate() { return lastVisitDate; }
    public void setLastVisitDate(LocalDate lastVisitDate) { this.lastVisitDate = lastVisitDate; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
