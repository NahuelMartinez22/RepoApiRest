package com.martinez.dentist.appointments.services;

import com.martinez.dentist.appointments.controllers.AppointmentRequestDTO;
import com.martinez.dentist.appointments.controllers.AppointmentResponseDTO;

import java.util.List;

public interface AppointmentService {
    String createAppointment(AppointmentRequestDTO dto);
    List<AppointmentResponseDTO> getAllAppointments();
    String updateAppointmentState(Long id, String state);
    String updateAppointment(Long id, AppointmentRequestDTO dto);
    List<AppointmentResponseDTO> findAppointmentsByDni(String dni);
    List<AppointmentResponseDTO> getAppointmentsByProfessionalDni(String dni);


}
