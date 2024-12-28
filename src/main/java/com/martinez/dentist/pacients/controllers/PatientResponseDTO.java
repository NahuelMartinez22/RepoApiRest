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

    @JsonProperty("birth_date")
    private LocalDate birthDate;

    @JsonProperty("registration_date")
    private LocalDate registrationDate;

    @JsonProperty("last_visit_date")
    private LocalDate lastVisitDate;


    // Constructor completo
    public PatientResponseDTO(Long id, String fullName, String documentType, String documentNumber,
                              LocalDate birthDate, LocalDate registrationDate, LocalDate lastVisitDate) {
        this.id = id;
        this.fullName = fullName;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.birthDate = birthDate;
        this.registrationDate = registrationDate;
        this.lastVisitDate = lastVisitDate;
    }

    // Getters y Setters
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
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
}
