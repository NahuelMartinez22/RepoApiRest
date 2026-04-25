package com.martinez.dentist.patients.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
@Table(name = "health_insurances")
public class HealthInsurance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "contact_email")
    private String contactEmail;

    private String phone;

    private String note;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "healthInsurance", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<InsurancePlan> plans;

    public HealthInsurance() {}

    public HealthInsurance(String name, String contactEmail, String phone, String note) {
        this.name = name;
        this.contactEmail = contactEmail;
        this.phone = phone;
        this.note = note;
        this.isActive = true;
    }

}
