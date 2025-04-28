package com.martinez.dentist.professionals.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private ProfessionalState professionalState;

    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfessionalSchedule> schedules = new ArrayList<>();

    public Professional() {
        this.schedules = new ArrayList<>();
    }

    public Professional(String fullName, String documentType, String documentNumber,
                        String phone) {
        this.fullName = fullName;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.phone = phone;
        this.professionalState = ProfessionalState.ACTIVE;
        this.schedules = new ArrayList<>();
    }

    public void updateData(com.martinez.dentist.professionals.controllers.ProfessionalRequestDTO dto) {
        this.fullName = dto.getFullName();
        this.documentType = dto.getDocumentType();
        this.documentNumber = dto.getDocumentNumber();
        this.phone = dto.getPhone();
    }

    public void disable() {
        this.professionalState = ProfessionalState.DEACTIVATED;
    }

    public void enable() {
        this.professionalState = ProfessionalState.ACTIVE;
    }

    public boolean trabajaEsteDiaYHorario(LocalDateTime dateTime) {
        if (schedules == null || schedules.isEmpty()) {
            return false;
        }

        DayOfWeek appointmentDay = dateTime.getDayOfWeek();
        LocalTime appointmentTime = dateTime.toLocalTime();

        return schedules.stream().anyMatch(schedule ->
                schedule.getDayOfWeek() == appointmentDay &&
                        !appointmentTime.isBefore(schedule.getStartTime()) &&
                        !appointmentTime.isAfter(schedule.getEndTime())
        );
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getDocumentType() { return documentType; }
    public String getDocumentNumber() { return documentNumber; }
    public String getPhone() { return phone; }
    public ProfessionalState getProfessionalState() { return professionalState; }
    public List<ProfessionalSchedule> getSchedules() { return schedules; }
}
