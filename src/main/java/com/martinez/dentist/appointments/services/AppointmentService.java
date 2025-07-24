package com.martinez.dentist.appointments.services;

import com.martinez.dentist.appointments.controllers.AppointmentRequestDTO;
import com.martinez.dentist.appointments.controllers.AppointmentResponseDTO;

import java.util.List;
import java.util.Map;

public interface AppointmentService {
    String createAppointment(AppointmentRequestDTO dto);
    List<AppointmentResponseDTO> getAllAppointments();
    String updateAppointmentState(Long id, String state);
    String updateAppointment(Long id, AppointmentRequestDTO dto);
    List<AppointmentResponseDTO> findAppointmentsByDni(String dni);
    List<AppointmentResponseDTO> getAppointmentsByProfessionalDni(String dni);
    String deleteAppointment(Long id);
    String confirmAppointment(Long id);
    String cancelAppointment(Long id);

    void markAsAttended(Long appointmentId, String credentialToken);
    Map<String, Object> getAppointmentsForBilling(Long obraSocialId, int mes, int anio);

    boolean confirmByToken(String token);
    boolean cancelByToken(String token);
}
