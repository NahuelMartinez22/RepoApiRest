package com.martinez.dentist.metrics.services;

import com.martinez.dentist.appointments.repositories.AppointmentRepository;
import com.martinez.dentist.metrics.controlles.MetricsResponseDTO;
import com.martinez.dentist.patients.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MetricsServiceImpl implements MetricsService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public Map<LocalDate, Long> getAppointmentsPerDay() {
        LocalDateTime desde = LocalDateTime.now().minusDays(7);
        return appointmentRepository.countAppointmentsByDayLastWeek(desde)
                .stream()
                .collect(Collectors.toMap(
                        row -> ((java.sql.Date) row[0]).toLocalDate(),
                        row -> (Long) row[1]
                ));
    }

    @Override
    public Map<String, Integer> getNewPatientsPerMonth() {
        LocalDate desde = LocalDate.now().minusMonths(6);
        return patientRepository.countNewPatientsPerMonth(desde)
                .stream()
                .sorted((a, b) -> Integer.compare(((Number) a[0]).intValue(), ((Number) b[0]).intValue()))
                .collect(Collectors.toMap(
                        row -> getMonthName(((Number) row[0]).intValue()),
                        row -> ((Number) row[1]).intValue(),
                        (v1, v2) -> v1,
                        LinkedHashMap::new
                ));
    }

    private String getMonthName(int monthNumber) {
        return java.time.Month.of(monthNumber).getDisplayName(java.time.format.TextStyle.FULL, new java.util.Locale("es"));
    }

    @Override
    public Map<String, Long> getAppointmentStateStats() {
        LocalDateTime desde = LocalDateTime.now().minusDays(30);

        return appointmentRepository.countAppointmentsByStateSince(desde)
                .stream()
                .collect(Collectors.toMap(
                        row -> row[0].toString(),
                        row -> (Long) row[1]
                ));
    }
}
