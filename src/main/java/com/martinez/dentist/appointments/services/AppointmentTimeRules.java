package com.martinez.dentist.appointments.services;

import com.martinez.dentist.appointments.models.Appointment;
import com.martinez.dentist.appointments.models.AppointmentState;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

// Reglas de duración y superposición de turnos.
// Los turnos sin hora de fin (los cargados antes de que existiera la duración) duran 15 minutos.
public final class AppointmentTimeRules {

    public static final int SLOT_MINUTES = 15;
    public static final int MAX_DURATION_MINUTES = 120;

    private AppointmentTimeRules() {}

    // Devuelve la hora de fin a usar: la indicada o, si no hay, inicio + 15 minutos.
    public static LocalDateTime resolveEnd(LocalDateTime start, LocalDateTime end) {
        LocalDateTime resolved = end != null ? end : start.plusMinutes(SLOT_MINUTES);
        long minutes = Duration.between(start, resolved).toMinutes();

        if (minutes <= 0) {
            throw new IllegalArgumentException("La hora de fin del turno debe ser posterior a la de inicio.");
        }
        if (minutes % SLOT_MINUTES != 0 || minutes > MAX_DURATION_MINUTES) {
            throw new IllegalArgumentException("La duración del turno debe ser de 15 a 120 minutos, en pasos de 15.");
        }
        return resolved;
    }

    public static LocalDateTime endOf(Appointment appointment) {
        return appointment.getEndDateTime() != null
                ? appointment.getEndDateTime()
                : appointment.getDateTime().plusMinutes(SLOT_MINUTES);
    }

    // Busca un turno activo que se superponga con [start, end), ignorando el turno que se está editando.
    public static Optional<Appointment> findOverlap(Collection<Appointment> candidates,
                                                    LocalDateTime start, LocalDateTime end,
                                                    Long ignoreId) {
        return candidates.stream()
                .filter(a -> ignoreId == null || !Objects.equals(a.getId(), ignoreId))
                .filter(a -> a.getState() != AppointmentState.CANCELADO)
                .filter(a -> start.isBefore(endOf(a)) && end.isAfter(a.getDateTime()))
                .findFirst();
    }
}
