package com.martinez.dentist.metrics.services;

import com.martinez.dentist.appointments.repositories.AppointmentRepository;
import com.martinez.dentist.metrics.controlles.MetricsResponseDTO;
import com.martinez.dentist.patients.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Map<Integer, Long> getNewPatientsPerWeek() {
        LocalDate desde = LocalDate.now().minusDays(28);
        return patientRepository.countNewPatientsPerWeek(desde)
                .stream()
                .collect(Collectors.toMap(
                        row -> (Integer) row[0],
                        row -> (Long) row[1]
                ));
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
