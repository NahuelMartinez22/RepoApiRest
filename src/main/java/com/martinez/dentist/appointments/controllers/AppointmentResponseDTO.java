package com.martinez.dentist.appointments.controllers;

import com.martinez.dentist.patients.controllers.PatientResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class AppointmentResponseDTO {

    private Long id;
    private Long patientId;
    private PatientResponseDTO patient;
    private LocalDateTime dateTime;
    private LocalDateTime endDateTime;
    private String professionalFullName;
    private String reason;
    private String note;
    private String state;

    public AppointmentResponseDTO() {}

    public AppointmentResponseDTO(Long id, Long patientId, PatientResponseDTO patient,
                                  LocalDateTime dateTime, LocalDateTime endDateTime, String professionalFullName,
                                  String reason, String note, String state) {
        this.id = id;
        this.patientId = patientId;
        this.patient = patient;
        this.dateTime = dateTime;
        this.endDateTime = endDateTime;
        this.professionalFullName = professionalFullName;
        this.reason = reason;
        this.note = note;
        this.state = state;
    }
}
