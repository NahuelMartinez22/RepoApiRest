package com.martinez.dentist.patients.controllers;

import java.time.LocalDateTime;
import java.util.List;

public class ClinicalHistoryResponseDTO {

    private Long id;
    private String patientFullName;
    private String professionalFullName;
    private LocalDateTime dateTime;
    private String description;
    private List<Long> procedureIds;
    private List<String> procedureNames;
    private List<ClinicalFileDTO> files;

    public ClinicalHistoryResponseDTO() {}

    public ClinicalHistoryResponseDTO(Long id, String patientFullName, String professionalFullName,
                                      LocalDateTime dateTime, String description,
                                      List<Long> procedureIds, List<String> procedureNames,
                                      List<ClinicalFileDTO> files) {
        this.id = id;
        this.patientFullName = patientFullName;
        this.professionalFullName = professionalFullName;
        this.dateTime = dateTime;
        this.description = description;
        this.procedureIds = procedureIds;
        this.procedureNames = procedureNames;
        this.files = files;
    }

    public Long getId() { return id; }

    public String getPatientFullName() { return patientFullName; }

    public String getProfessionalFullName() { return professionalFullName; }

    public LocalDateTime getDateTime() { return dateTime; }

    public String getDescription() { return description; }

    public List<Long> getProcedureIds() { return procedureIds; }

    public List<String> getProcedureNames() { return procedureNames; }

    public List<ClinicalFileDTO> getFiles() { return files; }

    public void setFiles(List<ClinicalFileDTO> files) {
        this.files = files;
    }

    public void setProcedureIds(List<Long> procedureIds) {
        this.procedureIds = procedureIds;
    }

    public void setProcedureNames(List<String> procedureNames) {
        this.procedureNames = procedureNames;
    }
}
