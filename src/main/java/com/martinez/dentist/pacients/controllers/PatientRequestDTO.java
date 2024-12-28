package com.martinez.dentist.pacients.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;


public class PatientRequestDTO {

    @JsonProperty("name")
    private String name;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("document_type")
    private String documentType;

    @JsonProperty("document_number")
    private String documentNumber;

    @JsonProperty("birth_date")
    private LocalDate birthDate;

    @JsonProperty("registrationDate")
    private LocalDate registrationDate;

    @JsonProperty("lastAppointmentDate")
    private LocalDate lastAppointmentDate;


    public PatientRequestDTO() {
    }

    // Constructor con todos los campos
    public PatientRequestDTO(String name, String lastName, String documentType, String documentNumber, LocalDate birthDate, LocalDate registrationDate, LocalDate lastAppointmentDate) {
        this.name = name;
        this.lastName = lastName;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.birthDate = birthDate;
        this.registrationDate = registrationDate;
        this.lastAppointmentDate = lastAppointmentDate;
    }

    // Getters y setters
    public String getFullName() {
        return this.name + " " + this.lastName;
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

    public LocalDate getLastAppointmentDate() {
        return lastAppointmentDate;
    }

    public void setLastAppointmentDate(LocalDate lastAppointmentDate) {
        this.lastAppointmentDate = lastAppointmentDate;
    }

    @Override
    public String toString() {
        return "PatientDTO{" +
                "fullName='" + getFullName() + '\'' +
                ", documentType='" + documentType + '\'' +
                ", documentNumber='" + documentNumber + '\'' +
                ", birthDate=" + birthDate +
                ", registrationDate=" + registrationDate +
                ", lastAppointmentDate=" + lastAppointmentDate +
                '}';
    }
}