package com.martinez.dentist.patients.controllers;

public class ClinicalHistoryRequestDTO {

    private String patientDocumentNumber;
    private Long professionalId;
    private String description;
    private Long procedureId;

    public ClinicalHistoryRequestDTO() {}

    public ClinicalHistoryRequestDTO(String patientDocumentNumber, Long professionalId, String description, Long procedureId) {
        this.patientDocumentNumber = patientDocumentNumber;
        this.professionalId = professionalId;
        this.description = description;
        this.procedureId = procedureId;
    }

    public String getPatientDocumentNumber() { return patientDocumentNumber; }
    public Long getProfessionalId() { return professionalId; }
    public String getDescription() { return description; }
    public Long getProcedureId() { return procedureId; }
}
