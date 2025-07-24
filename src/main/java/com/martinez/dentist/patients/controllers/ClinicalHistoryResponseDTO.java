package com.martinez.dentist.patients.controllers;

import java.time.LocalDateTime;
import java.util.List;

public class ClinicalHistoryResponseDTO {

    private Long id;
    private String patientFullName;
    private String professionalFullName;
    private LocalDateTime dateTime;
    private String description;
    private Long procedureId;
    private String procedureName;
    private List<ClinicalFileDTO> files;

    public ClinicalHistoryResponseDTO() {}

    public ClinicalHistoryResponseDTO(Long id, String patientFullName, String professionalFullName,
                                      LocalDateTime dateTime, String description,
                                      Long procedureId, String procedureName,
                                      List<ClinicalFileDTO> files) {
        this.id = id;
        this.patientFullName = patientFullName;
        this.professionalFullName = professionalFullName;
        this.dateTime = dateTime;
        this.description = description;
        this.procedureId = procedureId;
        this.procedureName = procedureName;
        this.files = files;
    }

    public Long getId() { return id; }
    public String getPatientFullName() { return patientFullName; }
    public String getProfessionalFullName() { return professionalFullName; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getDescription() { return description; }
    public Long getProcedureId() { return procedureId; }
    public String getProcedureName() { return procedureName; }
    public List<ClinicalFileDTO> getFiles() { return files; }

    public void setFiles(List<ClinicalFileDTO> files) { this.files = files; }
}
