package com.martinez.dentist.patients.controllers;

import java.time.LocalDate;

public class ClinicalHistoryResponseDTO {

    private Long id;
    private String patientFullName;
    private String professionalFullName;
    private LocalDate date;
    private String description;

    public ClinicalHistoryResponseDTO() {}

    public ClinicalHistoryResponseDTO(Long id, String patientFullName, String professionalFullName,
                                      LocalDate date, String description) {
        this.id = id;
        this.patientFullName = patientFullName;
        this.professionalFullName = professionalFullName;
        this.date = date;
        this.description = description;
    }

    public Long getId() { return id; }
    public String getPatientFullName() { return patientFullName; }
    public String getProfessionalFullName() { return professionalFullName; }
    public LocalDate getDate() { return date; }
    public String getDescription() { return description; }
}