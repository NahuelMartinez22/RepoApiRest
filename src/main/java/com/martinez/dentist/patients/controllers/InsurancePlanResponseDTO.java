package com.martinez.dentist.patients.controllers;

public class InsurancePlanResponseDTO {

    private Long id;
    private String name;
    private String healthInsuranceName;

    public InsurancePlanResponseDTO(Long id, String name, String healthInsuranceName) {
        this.id = id;
        this.name = name;
        this.healthInsuranceName = healthInsuranceName;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getHealthInsuranceName() { return healthInsuranceName; }
}
