package com.martinez.dentist.patients.controllers.InsurancePlan;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class InsurancePlanRequestDTO {

    @NotBlank(message = "El nombre del plan es obligatorio")
    private String name;

    @NotNull(message = "Debe indicar el ID de la obra social")
    private Long healthInsuranceId;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getHealthInsuranceId() { return healthInsuranceId; }
    public void setHealthInsuranceId(Long healthInsuranceId) { this.healthInsuranceId = healthInsuranceId; }
}
