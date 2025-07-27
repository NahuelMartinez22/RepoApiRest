package com.martinez.dentist.patients.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;
import com.martinez.dentist.patients.models.InsurancePlan;


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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setPlans(List<InsurancePlan> plans) {
        this.plans = plans;
    }

    public Boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(Boolean active) {

        this.isActive = active;
    }

    public List<InsurancePlan> getPlans() {
        return plans;
    }
}
