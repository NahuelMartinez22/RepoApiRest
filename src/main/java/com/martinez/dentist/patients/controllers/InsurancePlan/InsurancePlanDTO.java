package com.martinez.dentist.patients.controllers.InsurancePlan;

public class InsurancePlanDTO {
    private Long id;
    private String name;

    public InsurancePlanDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
