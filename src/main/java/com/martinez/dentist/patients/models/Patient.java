package com.martinez.dentist.patients.models;

import com.martinez.dentist.patients.controllers.PatientRequestDTO;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "document_number")
    private String documentNumber;

    @Column(name = "health_insurance")
    private String healthInsurance;

    @Column(name = "insurance_plan")
    private String insurancePlan;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "last_visit_date")
    private LocalDate lastVisitDate;

    @Column(name = "note")
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private PatientState patientState;

    protected Patient() {
    }

    public Patient(String fullName, String documentType, String documentNumber,
                   String healthInsurance, String insurancePlan, String phone,
                   String email, LocalDate registrationDate, LocalDate lastVisitDate, String note) {
        this.fullName = fullName;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.healthInsurance = healthInsurance;
        this.insurancePlan = insurancePlan;
        this.phone = phone;
        this.email = email;
        this.registrationDate = registrationDate;
        this.lastVisitDate = lastVisitDate;
        this.note = note;
        this.patientState = PatientState.ACTIVE;
    }

    @PrePersist
    public void prePersist() {
        if (this.registrationDate == null) {
            this.registrationDate = LocalDate.now();
        }
    }

    public void updateData(PatientRequestDTO dto) {
        this.fullName = dto.getFullName();
        this.documentType = dto.getDocumentType();
        this.documentNumber = dto.getDocumentNumber();
        this.healthInsurance = dto.getHealthInsurance();
        this.insurancePlan = dto.getInsurancePlan();
        this.phone = dto.getPhone();
        this.email = dto.getEmail();
        this.registrationDate = dto.getRegistrationDate();
        this.lastVisitDate = dto.getLastVisitDate();
        this.note = dto.getNote();
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getHealthInsurance() {
        return healthInsurance;
    }

    public String getInsurancePlan() {
        return insurancePlan;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public LocalDate getLastVisitDate() {
        return lastVisitDate;
    }

    public void setLastVisitDate(LocalDate lastVisitDate) {
        this.lastVisitDate = lastVisitDate;
    }

    public String getNote() {
        return note;
    }

    public PatientState getPatientState() {
        return patientState;
    }

    public void disablePatient() {
        this.patientState = PatientState.DEACTIVATED;
    }

    public void enablePatient() {
        this.patientState = PatientState.ACTIVE;
    }
}
