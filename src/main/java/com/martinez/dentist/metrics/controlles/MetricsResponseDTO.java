package com.martinez.dentist.metrics.controlles;

import java.time.LocalDate;
import java.util.Map;

public class MetricsResponseDTO {
    private Map<LocalDate, Long> appointmentsPerDay;
    private Map<String, String> newPatientsPerMonth;
    private Map<String, Long> appointmentStateCounts;

    public MetricsResponseDTO(Map<LocalDate, Long> appointmentsPerDay,
                              Map<String, String> newPatientsPerMonth,
                              Map<String, Long> appointmentStateCounts) {
        this.appointmentsPerDay = appointmentsPerDay;
        this.newPatientsPerMonth = newPatientsPerMonth;
        this.appointmentStateCounts = appointmentStateCounts;
    }

    public Map<LocalDate, Long> getAppointmentsPerDay() {
        return appointmentsPerDay;
    }

    public Map<String, String> getNewPatientsPerMonth() {
        return newPatientsPerMonth;
    }

    public Map<String, Long> getAppointmentStateCounts() {
        return appointmentStateCounts;
    }
}
