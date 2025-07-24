package com.martinez.dentist.appointments.models;

import com.martinez.dentist.patients.models.DentalProcedure;
import com.martinez.dentist.professionals.models.Professional;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

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

    @Column(name = "reminder_sent")
    private boolean reminderSent = false;

    @ManyToMany
    @JoinTable(
            name = "appointment_procedures",
            joinColumns = @JoinColumn(name = "appointment_id"),
            inverseJoinColumns = @JoinColumn(name = "procedure_id")
    )
    private List<DentalProcedure> procedures;

    @Column(name = "credential_token")
    private String credentialToken;

    @Column(name = "cancel_token", unique = true)
    private String cancelToken;

    @Column(name = "confirm_token", unique = true)
    private String confirmToken;


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


    public void updateData(String dni, LocalDateTime dateTime, Professional professional,
                           String reason, AppointmentState state) {
        this.patientDni = dni;
        this.dateTime = dateTime;
        this.professional = professional;
        this.reason = reason;
        this.state = state;
    }

    public void updateState(AppointmentState newState) {
        this.state = newState;
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

    public void setState(AppointmentState state) {
        this.state = state;
    }

    public boolean isReminderSent() {
        return reminderSent;
    }

    public void setReminderSent(boolean reminderSent) {
        this.reminderSent = reminderSent;
    }

    public List<DentalProcedure> getProcedures() {
        return procedures;
    }

    public void setProcedures(List<DentalProcedure> procedures) {
        this.procedures = procedures;
    }

    public String getCredentialToken() {return credentialToken;}
    public void registrarCredentialToken(String token) {
        this.credentialToken = token;
    }

    public String getCancelToken() {
        return cancelToken;
    }

    public void setCancelToken(String cancelToken) {
        this.cancelToken = cancelToken;
    }

    public String getConfirmToken() {
        return confirmToken;
    }

    public void setConfirmToken(String confirmToken) {
        this.confirmToken = confirmToken;
    }
}
