package com.martinez.dentist.appointments.models;

import com.martinez.dentist.professionals.models.Professional;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_dni", nullable = false)
    private String patientDni;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "professional_id", nullable = false)
    private Professional professional;

    @Column(name = "reason")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private AppointmentState state;

    public Appointment() {}

    public Appointment(String patientDni, LocalDateTime dateTime,
                       Professional professional, String reason,
                       AppointmentState state) {
        this.patientDni = patientDni;
        this.dateTime = dateTime;
        this.professional = professional;
        this.reason = reason;
        this.state = state;
    }

    public void updateData(String patientDni, LocalDateTime dateTime, Professional professional,
                           String reason, AppointmentState state) {
        this.patientDni = patientDni;
        this.dateTime = dateTime;
        this.professional = professional;
        this.reason = reason;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public String getPatientDni() {
        return patientDni;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Professional getProfessional() {
        return professional;
    }

    public String getReason() {
        return reason;
    }

    public AppointmentState getState() {
        return state;
    }

    public void updateState(AppointmentState newState) {
        this.state = newState;
    }
}
