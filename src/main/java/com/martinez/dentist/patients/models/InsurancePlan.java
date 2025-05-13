package com.martinez.dentist.patients.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "insurance_plans")
public class InsurancePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "health_insurance_id", nullable = false)
    @JsonBackReference
    private HealthInsurance healthInsurance;

    public InsurancePlan() {}

    public InsurancePlan(String name, com.martinez.dentist.patients.models.HealthInsurance healthInsurance) {
        this.name = name;
        this.healthInsurance = healthInsurance;
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public com.martinez.dentist.patients.models.HealthInsurance getHealthInsurance() {
        return healthInsurance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHealthInsurance(HealthInsurance healthInsurance) {
        this.healthInsurance = healthInsurance;
    }
}
