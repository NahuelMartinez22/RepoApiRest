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

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "last_visit_date")
    private LocalDate lastVisitDate;

    @Enumerated(EnumType.STRING) // Guarda el valor como texto en la base de datos
    @Column(name = "state", nullable = false)
    private PatientState patientState;

    //Constructor vacio
    public Patient() {
    }

    public Patient(String fullName, String documentType, String documentNumber, LocalDate birthDate, LocalDate registrationDate, LocalDate lastVisitDate) {
        this.fullName = fullName;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.birthDate = birthDate;
        this.registrationDate = registrationDate;
        this.lastVisitDate = lastVisitDate;
        this.patientState = PatientState.ACTIVE;
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

    public LocalDate getBirthDate() {
        return birthDate;
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

    public void disablePatient() {
        patientState = PatientState.DEACTIVATED;
    }
    public void enablePatient() {
        patientState = PatientState.ACTIVE;
    }
}
