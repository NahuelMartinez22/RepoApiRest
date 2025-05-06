package com.martinez.dentist.patients.models;

import com.martinez.dentist.professionals.models.Professional;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clinical_histories")
public class ClinicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne(optional = false)
    @JoinColumn(name = "professional_id")
    private Professional professional;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    public ClinicalHistory() {}

    public ClinicalHistory(Patient patient, Professional professional, LocalDate date, String description) {
        this.patient = patient;
        this.professional = professional;
        this.date = date;
        this.description = description;
    }


    public Long getId() { return id; }
    public Patient getPatient() { return patient; }
    public Professional getProfessional() { return professional; }
    public LocalDate getDate() { return date; }
    public String getDescription() { return description; }

    public void setPatient(Patient patient) { this.patient = patient; }
    public void setProfessional(Professional professional) { this.professional = professional; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setDescription(String description) { this.description = description; }

    @OneToMany(mappedBy = "clinicalHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClinicalFile> files = new ArrayList<>();

    public List<ClinicalFile> getFiles() {
        return files;
    }

    public void addFile(ClinicalFile file) {
        files.add(file);
        file.setClinicalHistory(this);
    }

    public void removeFile(ClinicalFile file) {
        files.remove(file);
        file.setClinicalHistory(null);
    }
}
