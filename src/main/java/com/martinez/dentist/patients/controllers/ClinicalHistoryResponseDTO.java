package com.martinez.dentist.patients.controllers;

import java.time.LocalDate;
import java.util.List;

public class ClinicalHistoryResponseDTO {

    private Long id;
    private String patientFullName;
    private String professionalFullName;
    private LocalDate date;
    private String description;
    private List<ClinicalFileDTO> files;

    public ClinicalHistoryResponseDTO() {}

    public ClinicalHistoryResponseDTO(Long id, String patientFullName, String professionalFullName,
                                      LocalDate date, String description, List<ClinicalFileDTO> files) {
        this.id = id;
        this.patientFullName = patientFullName;
        this.professionalFullName = professionalFullName;
        this.date = date;
        this.description = description;
        this.files = files;
    }

    public Long getId() { return id; }
    public String getPatientFullName() { return patientFullName; }
    public String getProfessionalFullName() { return professionalFullName; }
    public LocalDate getDate() { return date; }
    public String getDescription() { return description; }
    public List<ClinicalFileDTO> getFiles() { return files; }

    public void setFiles(List<ClinicalFileDTO> files) { this.files = files; }
}
