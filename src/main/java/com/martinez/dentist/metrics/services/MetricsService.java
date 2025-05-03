package com.martinez.dentist.metrics.services;

import com.martinez.dentist.metrics.controlles.MetricsResponseDTO;

import java.time.LocalDate;
import java.util.Map;

public interface MetricsService {
    Map<LocalDate, Long> getAppointmentsPerDay();
    Map<String, String> getNewPatientsPerMonth();
    Map<String, Long> getAppointmentStateStats();
}

