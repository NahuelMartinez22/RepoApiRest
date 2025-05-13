package com.martinez.dentist.appointments.controllers;

import com.martinez.dentist.appointments.models.AppointmentState;

import java.time.LocalDateTime;
import java.util.List;

public class AppointmentRequestDTO {

    private String patientDni;
    private LocalDateTime dateTime;
    private Long professionalId;
    private String reason;
    private AppointmentState state;

    private List<Long> procedureIds;

    public AppointmentRequestDTO() {}

    public AppointmentRequestDTO(String patientDni, LocalDateTime dateTime,
                                 Long professionalId, String reason,
                                 AppointmentState state, List<Long> procedureIds) {
        this.patientDni = patientDni;
        this.dateTime = dateTime;
        this.professionalId = professionalId;
        this.reason = reason;
        this.state = state;
        this.procedureIds = procedureIds;
    }

    public String getPatientDni() {
        return patientDni;
    }

    public void setPatientDni(String patientDni) {
        this.patientDni = patientDni;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Long getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(Long professionalId) {
        this.professionalId = professionalId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public AppointmentState getState() {
        return state;
    }

    public void setState(AppointmentState state) {
        this.state = state;
    }

    public List<Long> getProcedureIds() {
        return procedureIds;
    }

    public void setProcedureIds(List<Long> procedureIds) {
        this.procedureIds = procedureIds;
    }
}
