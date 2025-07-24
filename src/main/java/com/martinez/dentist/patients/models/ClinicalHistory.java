package com.martinez.dentist.patients.models;

import com.martinez.dentist.professionals.models.Professional;
import jakarta.persistence.*;

import java.time.LocalDateTime;
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

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_procedure", nullable = false)
    private DentalProcedure procedure;

    @Column(name = "description", columnDefinition = "TEXT", nullable = true)
    private String description;

    @OneToMany(mappedBy = "clinicalHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClinicalFile> files = new ArrayList<>();

    // Constructors
    public ClinicalHistory() {}

    public ClinicalHistory(Patient patient, Professional professional, LocalDateTime dateTime, String description) {
        this.patient = patient;
        this.professional = professional;
        this.dateTime = dateTime;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() { return id; }

    public Patient getPatient() { return patient; }

    public Professional getProfessional() { return professional; }

    public LocalDateTime getDateTime() { return dateTime; }

    public DentalProcedure getProcedure() { return procedure; }

    public String getDescription() { return description; }

    public List<ClinicalFile> getFiles() { return files; }

    public void setPatient(Patient patient) { this.patient = patient; }

    public void setProfessional(Professional professional) { this.professional = professional; }

    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public void setProcedure(DentalProcedure procedure) { this.procedure = procedure; }

    public void setDescription(String description) { this.description = description; }


    public void addFile(ClinicalFile file) {
        files.add(file);
        file.setClinicalHistory(this);
    }

    public void removeFile(ClinicalFile file) {
        files.remove(file);
        file.setClinicalHistory(null);
    }
}
