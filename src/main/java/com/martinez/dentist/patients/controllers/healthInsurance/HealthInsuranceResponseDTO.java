package com.martinez.dentist.patients.controllers.healthInsurance;

import com.martinez.dentist.patients.controllers.InsurancePlanDTO;

import java.util.List;

public class HealthInsuranceResponseDTO {
    private Long id;
    private String name;
    private String contactEmail;
    private String phone;
    private String note;
    private Boolean isActive;
    private List<InsurancePlanDTO> plans;

    public HealthInsuranceResponseDTO(Long id, String name, String contactEmail, String phone, String note, Boolean isActive, List<InsurancePlanDTO> plans) {
        this.id = id;
        this.name = name;
        this.contactEmail = contactEmail;
        this.phone = phone;
        this.note = note;
        this.isActive = isActive;
        this.plans = plans;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getPhone() {
        return phone;
    }

    public String getNote() {
        return note;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public List<InsurancePlanDTO> getPlans() {
        return plans;
    }
}
