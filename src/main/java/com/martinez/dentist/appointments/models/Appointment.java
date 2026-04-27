package com.martinez.dentist.appointments.models;

import com.martinez.dentist.patients.models.Patient;
import com.martinez.dentist.practices.models.Practice;
import com.martinez.dentist.professionals.models.Professional;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(name = "patient_dni", nullable = false)
    private String patientDni;

    @Getter
    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_id", nullable = false)
    private Professional professional;

    @Getter
    @Column(name = "reason")
    private String reason;

    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private AppointmentState state;

    @Setter
    @Getter
    @Column(name = "reminder_sent")
    private boolean reminderSent = false;

    @Getter
    @Column(name = "credential_token")
    private String credentialToken;

    @Setter
    @Getter
    @Column(name = "cancel_token", unique = true)
    private String cancelToken;

    @Setter
    @Getter
    @Column(name = "confirm_token", unique = true)
    private String confirmToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "practice_id", nullable = false)
    private Practice practice;

    public Appointment() {}

    public Appointment(String patientDni, LocalDateTime dateTime,
                       Professional professional, String reason,
                       AppointmentState state, Patient patient, Practice practice) {
        this.patientDni = patientDni;
        this.dateTime = dateTime;
        this.professional = professional;
        this.reason = reason;
        this.state = state;
        this.patient = patient;
        this.practice = practice;
    }


    public void updateData(String dni, LocalDateTime dateTime, Professional professional,
                           String reason, AppointmentState state, Patient patient) {
        this.patientDni = dni;
        this.dateTime = dateTime;
        this.professional = professional;
        this.reason = reason;
        this.state = state;
        this.patient = patient;
    }

    public void updateState(AppointmentState newState) {
        this.state = newState;
    }

    public void registrarCredentialToken(String token) {
        this.credentialToken = token;
    }
}
