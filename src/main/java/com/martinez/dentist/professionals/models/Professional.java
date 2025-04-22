package com.martinez.dentist.professionals.models;

import com.martinez.dentist.professionals.controllers.ProfessionalRequestDTO;
import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "professionals")
public class Professional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "document_number", unique = true)
    private String documentNumber;

    @Column(name = "phone")
    private String phone;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private ProfessionalState professionalState;

    public Professional() {}

    public Professional(String fullName, String documentType, String documentNumber,
                        String phone, LocalTime startTime, LocalTime endTime) {
        this.fullName = fullName;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.phone = phone;
        this.startTime = startTime;
        this.endTime = endTime;
        this.professionalState = ProfessionalState.ACTIVE;
    }

    public void updateData(ProfessionalRequestDTO dto) {
        this.fullName = dto.getFullName();
        this.documentType = dto.getDocumentType();
        this.documentNumber = dto.getDocumentNumber();
        this.phone = dto.getPhone();
        this.startTime = dto.getStartTime();
        this.endTime = dto.getEndTime();
    }

    public void disable() {
        this.professionalState = ProfessionalState.DEACTIVATED;
    }

    // Getters
    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getDocumentType() { return documentType; }
    public String getDocumentNumber() { return documentNumber; }
    public String getPhone() { return phone; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public ProfessionalState getProfessionalState() { return professionalState; }
    public void enable() {this.professionalState = ProfessionalState.ACTIVE;}
}