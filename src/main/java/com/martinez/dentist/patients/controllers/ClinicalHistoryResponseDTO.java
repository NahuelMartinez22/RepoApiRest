package com.martinez.dentist.patients.controllers;

import java.time.LocalDate;
import java.util.List;

public class ClinicalHistoryResponseDTO {

    private Long id;
    private String patientFullName;
    private String professionalFullName;
    private LocalDate date;
    private String description;
    private Long procedureId;
    private String procedureName;
    private List<ClinicalFileDTO> files;

    public ClinicalHistoryResponseDTO() {}

    public ClinicalHistoryResponseDTO(Long id, String patientFullName, String professionalFullName,
                                      LocalDate date, String description,
                                      Long procedureId, String procedureName,
                                      List<ClinicalFileDTO> files) {
        this.id = id;
        this.patientFullName = patientFullName;
        this.professionalFullName = professionalFullName;
        this.date = date;
        this.description = description;
        this.procedureId = procedureId;
        this.procedureName = procedureName;
        this.files = files;
    }

    public Long getId() { return id; }
    public String getPatientFullName() { return patientFullName; }
    public String getProfessionalFullName() { return professionalFullName; }
    public LocalDate getDate() { return date; }
    public String getDescription() { return description; }
    public Long getProcedureId() { return procedureId; }
    public String getProcedureName() { return procedureName; }
    public List<ClinicalFileDTO> getFiles() { return files; }

    public void setFiles(List<ClinicalFileDTO> files) { this.files = files; }
}
