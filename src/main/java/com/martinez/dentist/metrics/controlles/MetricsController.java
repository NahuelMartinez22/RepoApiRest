package com.martinez.dentist.metrics.controlles;

import com.martinez.dentist.metrics.services.MetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/metrics")
public class MetricsController {

    @Autowired
    private MetricsService metricsService;

    @GetMapping("/appointments-per-day")
    public ResponseEntity<Map<LocalDate, Long>> getAppointmentsPerDay() {
        return ResponseEntity.ok(metricsService.getAppointmentsPerDay());
    }

    @GetMapping("/new-patients-per-week")
    public ResponseEntity<Map<Integer, Long>> getNewPatientsPerWeek() {
        return ResponseEntity.ok(metricsService.getNewPatientsPerWeek());
    }

    @GetMapping("/appointment-stats")
    public ResponseEntity<Map<String, Long>> getAppointmentStats() {
        return ResponseEntity.ok(metricsService.getAppointmentStateStats());
    }
}

