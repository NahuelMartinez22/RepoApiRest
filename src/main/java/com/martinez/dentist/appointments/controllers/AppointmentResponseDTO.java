package com.martinez.dentist.appointments.controllers;

import java.time.LocalDateTime;

public class AppointmentResponseDTO {
    private String patientDni;
    private String patientFullName;
    private LocalDateTime dateTime;
    private String doctorFullName;
    private String reason;
    private String state;

    public AppointmentResponseDTO(String patientDni, String patientFullName, LocalDateTime dateTime,
                                  String doctorFullName, String reason, String state) {
        this.patientDni = patientDni;
        this.patientFullName = patientFullName;
        this.dateTime = dateTime;
        this.doctorFullName = doctorFullName;
        this.reason = reason;
        this.state = state;
    }

    public String getPatientDni() { return patientDni; }
    public String getPatientFullName() { return patientFullName; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getDoctorFullName() { return doctorFullName; }
    public String getReason() { return reason; }
    public String getState() { return state; }
}
