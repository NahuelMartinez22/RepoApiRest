package com.martinez.dentist.appointments.controllers;

import java.time.LocalDateTime;

public class AppointmentResponseDTO {
    private String patientDni;
    private String patientFullName;
    private String healthInsurance;
    private String insurancePlan;
    private String phone;
    private String note;
    private LocalDateTime dateTime;
    private String doctorFullName;
    private String reason;
    private String state;

    public AppointmentResponseDTO(String patientDni, String patientFullName, String healthInsurance,
                                  String insurancePlan, String phone, String note,
                                  LocalDateTime dateTime, String doctorFullName,
                                  String reason, String state) {
        this.patientDni = patientDni;
        this.patientFullName = patientFullName;
        this.healthInsurance = healthInsurance;
        this.insurancePlan = insurancePlan;
        this.phone = phone;
        this.note = note;
        this.dateTime = dateTime;
        this.doctorFullName = doctorFullName;
        this.reason = reason;
        this.state = state;
    }

    public String getPatientDni() { return patientDni; }
    public String getPatientFullName() { return patientFullName; }
    public String getHealthInsurance() { return healthInsurance; }
    public String getInsurancePlan() { return insurancePlan; }
    public String getPhone() { return phone; }
    public String getNote() { return note; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getDoctorFullName() { return doctorFullName; }
    public String getReason() { return reason; }
    public String getState() { return state; }
}
