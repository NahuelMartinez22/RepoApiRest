package com.martinez.dentist.appointments.controllers;

import com.martinez.dentist.appointments.models.AppointmentState;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class AppointmentRequestDTO {

    private String patientDni;
    private LocalDateTime dateTime;
    private LocalDateTime endDateTime;
    private Long professionalId;
    private String reason;
    private String note;
    private AppointmentState state;

    public AppointmentRequestDTO() {}
}
