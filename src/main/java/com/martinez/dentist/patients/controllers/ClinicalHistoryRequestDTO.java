package com.martinez.dentist.patients.controllers;

import java.util.List;

public class ClinicalHistoryRequestDTO {

    private String patientDocumentNumber;
    private Long professionalId;
    private String description;
    private List<Long> procedureIds;

    public ClinicalHistoryRequestDTO() {}

    public ClinicalHistoryRequestDTO(String patientDocumentNumber, Long professionalId, String description, List<Long> procedureIds) {
        this.patientDocumentNumber = patientDocumentNumber;
        this.professionalId = professionalId;
        this.description = description;
        this.procedureIds = procedureIds;
    }

    public String getPatientDocumentNumber() {
        return patientDocumentNumber;
    }

    public Long getProfessionalId() {
        return professionalId;
    }

    public String getDescription() {
        return description;
    }

    public List<Long> getProcedureIds() {
        return procedureIds;
    }

    public void setPatientDocumentNumber(String patientDocumentNumber) {
        this.patientDocumentNumber = patientDocumentNumber;
    }

    public void setProfessionalId(Long professionalId) {
        this.professionalId = professionalId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProcedureIds(List<Long> procedureIds) {
        this.procedureIds = procedureIds;
    }
}
