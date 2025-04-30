package com.martinez.dentist.metrics.services;

import com.martinez.dentist.metrics.controlles.MetricsResponseDTO;

import java.time.LocalDate;
import java.util.Map;

public interface MetricsService {
    Map<LocalDate, Long> getAppointmentsPerDay();
    Map<Integer, Long> getNewPatientsPerWeek();
    Map<String, Long> getAppointmentStateStats();
}

