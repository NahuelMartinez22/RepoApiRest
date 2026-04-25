package com.martinez.dentist.patients.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "insurance_plans")
public class InsurancePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @ManyToOne
    @JoinColumn(name = "health_insurance_id", nullable = false)
    @JsonBackReference
    private HealthInsurance healthInsurance;

    public InsurancePlan() {}

    public InsurancePlan(String name, com.martinez.dentist.patients.models.HealthInsurance healthInsurance) {
        this.name = name;
        this.healthInsurance = healthInsurance;
    }
}
