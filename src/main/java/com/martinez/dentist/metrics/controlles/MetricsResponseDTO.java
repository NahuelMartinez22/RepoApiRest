package com.martinez.dentist.metrics.controlles;

import java.time.LocalDate;
import java.util.Map;

public class MetricsResponseDTO {
    private Map<LocalDate, Long> appointmentsPerDay;
    private Map<Integer, Long> newPatientsPerWeek;
    private Map<String, Long> appointmentStateCounts;

    public MetricsResponseDTO(Map<LocalDate, Long> appointmentsPerDay,
                              Map<Integer, Long> newPatientsPerWeek,
                              Map<String, Long> appointmentStateCounts) {
        this.appointmentsPerDay = appointmentsPerDay;
        this.newPatientsPerWeek = newPatientsPerWeek;
        this.appointmentStateCounts = appointmentStateCounts;
    }

    public Map<LocalDate, Long> getAppointmentsPerDay() { return appointmentsPerDay; }
    public Map<Integer, Long> getNewPatientsPerWeek() { return newPatientsPerWeek; }
    public Map<String, Long> getAppointmentStateCounts() { return appointmentStateCounts; }
}
