package com.martinez.dentist.professionals.controllers.professionalSpecialty;

public class ProfessionalSpecialtyResponseDTO {
    private Long id;
    private Long professionalId;
    private String professionalName;
    private Long specialtyId;
    private String specialtyName;

    public ProfessionalSpecialtyResponseDTO(Long id, Long professionalId, String professionalName,
                                            Long specialtyId, String specialtyName) {
        this.id = id;
        this.professionalId = professionalId;
        this.professionalName = professionalName;
        this.specialtyId = specialtyId;
        this.specialtyName = specialtyName;
    }

    public Long getId() {
        return id;
    }

    public Long getProfessionalId() {
        return professionalId;
    }

    public String getProfessionalName() {
        return professionalName;
    }

    public Long getSpecialtyId() {
        return specialtyId;
    }

    public String getSpecialtyName() {
        return specialtyName;
    }
}

