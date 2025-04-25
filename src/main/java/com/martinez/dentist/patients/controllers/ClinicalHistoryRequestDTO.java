package com.martinez.dentist.patients.controllers;

import java.time.LocalDate;

public class ClinicalHistoryRequestDTO {

    private String patientDocumentNumber;  // 👈 Ahora es String
    private Long professionalId;
    private LocalDate date;
    private String description;

    public ClinicalHistoryRequestDTO() {}

    public ClinicalHistoryRequestDTO(String patientDocumentNumber, Long professionalId, LocalDate date, String description) {
        this.patientDocumentNumber = patientDocumentNumber;
        this.professionalId = professionalId;
        this.date = date;
        this.description = description;
    }

    public String getPatientDocumentNumber() { return patientDocumentNumber; }  // 👈 getter correcto
    public Long getProfessionalId() { return professionalId; }
    public LocalDate getDate() { return date; }
    public String getDescription() { return description; }
}
