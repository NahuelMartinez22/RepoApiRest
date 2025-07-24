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

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "clinicalHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClinicalFile> files = new ArrayList<>();

    @OneToMany(mappedBy = "clinicalHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClinicalHistoryProcedure> clinicalHistoryProcedures = new ArrayList<>();


    public ClinicalHistory() {}

    public ClinicalHistory(Patient patient, Professional professional, LocalDateTime dateTime, String description) {
        this.patient = patient;
        this.professional = professional;
        this.dateTime = dateTime;
        this.description = description;
    }


    public Long getId() { return id; }
    public Patient getPatient() { return patient; }
    public Professional getProfessional() { return professional; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getDescription() { return description; }
    public List<ClinicalFile> getFiles() { return files; }
    public List<ClinicalHistoryProcedure> getClinicalHistoryProcedures() { return clinicalHistoryProcedures; }

    public void addProcedure(DentalProcedure procedure) {
        ClinicalHistoryProcedure chp = new ClinicalHistoryProcedure(this, procedure);
        clinicalHistoryProcedures.add(chp);
    }

    public void removeProcedure(DentalProcedure procedure) {
        clinicalHistoryProcedures.removeIf(chp -> chp.getProcedure().equals(procedure));
    }

    public List<DentalProcedure> getProcedures() {
        return clinicalHistoryProcedures.stream()
                .map(ClinicalHistoryProcedure::getProcedure)
                .toList();
    }

    public void setPatient(Patient patient) { this.patient = patient; }
    public void setProfessional(Professional professional) { this.professional = professional; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    public void setDescription(String description) { this.description = description; }

    public void setProcedures(List<DentalProcedure> procedures) {
        clinicalHistoryProcedures.clear();
        procedures.forEach(this::addProcedure);
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
