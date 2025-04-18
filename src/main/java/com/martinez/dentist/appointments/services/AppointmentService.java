package com.martinez.dentist.appointments.services;

import com.martinez.dentist.appointments.controllers.AppointmentRequestDTO;
import com.martinez.dentist.appointments.controllers.AppointmentResponseDTO;

import java.util.List;

public interface AppointmentService {
    String createAppointment(AppointmentRequestDTO request);
    AppointmentResponseDTO getAppointmentWithDetails(Long id);
    List<AppointmentResponseDTO> getAllAppointments();

}