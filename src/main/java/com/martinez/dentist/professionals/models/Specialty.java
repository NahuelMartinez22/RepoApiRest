package com.martinez.dentist.professionals.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "specialties")
public class Specialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @OneToMany(mappedBy = "specialty", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfessionalSpecialty> professionalSpecialties;

    // Getters y Setters
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

    public List<ProfessionalSpecialty> getProfessionalSpecialties() {
        return professionalSpecialties;
    }

    public void setProfessionalSpecialties(List<ProfessionalSpecialty> professionalSpecialties) {
        this.professionalSpecialties = professionalSpecialties;
    }
}
