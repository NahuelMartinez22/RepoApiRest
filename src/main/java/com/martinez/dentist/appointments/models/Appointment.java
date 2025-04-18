package com.martinez.dentist.appointments.models;

import com.martinez.dentist.appointments.models.AppointmentState;
import com.martinez.dentist.users.models.User;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctor;

    @Column(nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentState state;

    // Constructor vacío requerido por JPA
    public Appointment() {
    }

    // Constructor con parámetros
    public Appointment(String patientDni, LocalDateTime dateTime, User doctor, String reason, AppointmentState state) {
        this.patientDni = patientDni;
        this.dateTime = dateTime;
        this.doctor = doctor;
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

    public User getDoctor() {
        return doctor;
    }

    public String getReason() {
        return reason;
    }

    public AppointmentState getState() {
        return state;
    }
}
