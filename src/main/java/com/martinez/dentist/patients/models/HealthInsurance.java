package com.martinez.dentist.patients.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

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

    // Getters y setters

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

    public void setIsActive(Boolean active) {
        this.isActive = active;
    }

    public List<com.martinez.dentist.patients.models.InsurancePlan> getPlans() {
        return plans;
    }
}
