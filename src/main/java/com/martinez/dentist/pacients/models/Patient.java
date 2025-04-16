package com.martinez.dentist.pacients.models;

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

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "last_visit_date")
    private LocalDate lastVisitDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private PatientState patientState;

    protected Patient(){
    }

    public Patient(String fullName, String documentType, String documentNumber,
                   String healthInsurance, String insurancePlan, String phone,
                   LocalDate registrationDate, LocalDate lastVisitDate) {
        this.fullName = fullName;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.healthInsurance = healthInsurance;
        this.insurancePlan = insurancePlan;
        this.phone = phone;
        this.registrationDate = registrationDate;
        this.lastVisitDate = lastVisitDate;
        this.patientState = PatientState.ACTIVE;
    }

    // Getters
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

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public LocalDate getLastVisitDate() {
        return lastVisitDate;
    }

    public PatientState getPatientState() {
        return patientState;
    }

    // Métodos para cambiar el estado del paciente
    public void disablePatient() {
        this.patientState = PatientState.DEACTIVATED;
    }

    public void enablePatient() {
        this.patientState = PatientState.ACTIVE;
    }
}
