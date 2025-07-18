package com.martinez.dentist.professionals.controllers.professionalSpecialty;

public class ProfessionalSpecialtyRequestDTO {
    private Long professionalId;
    private Long specialtyId;

    public Long getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(Long professionalId) {
        this.professionalId = professionalId;
    }

    public Long getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(Long specialtyId) {
        this.specialtyId = specialtyId;
    }
}
