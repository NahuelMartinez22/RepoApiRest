package com.martinez.dentist.appointments.services;

import com.martinez.dentist.appointments.models.Appointment;
import com.martinez.dentist.appointments.models.AppointmentState;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AppointmentTimeRulesTest {

    private static final LocalDateTime NUEVE = LocalDateTime.of(2026, 10, 14, 9, 0);

    @Test
    void sinHoraDeFinDuraQuinceMinutos() {
        assertThat(AppointmentTimeRules.resolveEnd(NUEVE, null)).isEqualTo(NUEVE.plusMinutes(15));
    }

    @Test
    void aceptaQuinceTreintaCuarentaYCincoYSesentaMinutos() {
        for (int m : new int[]{15, 30, 45, 60}) {
            assertThat(AppointmentTimeRules.resolveEnd(NUEVE, NUEVE.plusMinutes(m))).isEqualTo(NUEVE.plusMinutes(m));
        }
    }

    @Test
    void rechazaDuracionesInvalidas() {
        assertThatThrownBy(() -> AppointmentTimeRules.resolveEnd(NUEVE, NUEVE)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> AppointmentTimeRules.resolveEnd(NUEVE, NUEVE.plusMinutes(20))).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> AppointmentTimeRules.resolveEnd(NUEVE, NUEVE.plusMinutes(75))).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> AppointmentTimeRules.resolveEnd(NUEVE, NUEVE.minusMinutes(15))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void detectaSuperposiciones() {
        // Turno existente de 9:00 a 9:30
        Appointment existente = turno(1L, NUEVE, NUEVE.plusMinutes(30), AppointmentState.CONFIRMADO);
        List<Appointment> turnos = List.of(existente);

        // 9:15-9:30 pisa
        assertThat(AppointmentTimeRules.findOverlap(turnos, NUEVE.plusMinutes(15), NUEVE.plusMinutes(30), null)).isPresent();
        // 8:45-9:15 pisa
        assertThat(AppointmentTimeRules.findOverlap(turnos, NUEVE.minusMinutes(15), NUEVE.plusMinutes(15), null)).isPresent();
        // 9:30-10:00 empieza justo cuando termina: no pisa
        assertThat(AppointmentTimeRules.findOverlap(turnos, NUEVE.plusMinutes(30), NUEVE.plusMinutes(60), null)).isEmpty();
        // 8:30-9:00 termina justo cuando empieza: no pisa
        assertThat(AppointmentTimeRules.findOverlap(turnos, NUEVE.minusMinutes(30), NUEVE, null)).isEmpty();
    }

    @Test
    void losTurnosViejosSinFinOcupanQuinceMinutos() {
        List<Appointment> turnos = List.of(turno(1L, NUEVE, null, AppointmentState.PENDIENTE));

        assertThat(AppointmentTimeRules.findOverlap(turnos, NUEVE.plusMinutes(10), NUEVE.plusMinutes(25), null)).isPresent();
        assertThat(AppointmentTimeRules.findOverlap(turnos, NUEVE.plusMinutes(15), NUEVE.plusMinutes(30), null)).isEmpty();
    }

    @Test
    void ignoraCanceladosYElTurnoQueSeEstaEditando() {
        List<Appointment> turnos = List.of(
                turno(1L, NUEVE, NUEVE.plusMinutes(30), AppointmentState.CANCELADO),
                turno(2L, NUEVE, NUEVE.plusMinutes(30), AppointmentState.PENDIENTE));

        assertThat(AppointmentTimeRules.findOverlap(turnos, NUEVE, NUEVE.plusMinutes(15), 2L)).isEmpty();
        assertThat(AppointmentTimeRules.findOverlap(turnos, NUEVE, NUEVE.plusMinutes(15), null)).isPresent();
    }

    private static Appointment turno(Long id, LocalDateTime start, LocalDateTime end, AppointmentState state) {
        Appointment a = mock(Appointment.class);
        when(a.getId()).thenReturn(id);
        when(a.getDateTime()).thenReturn(start);
        when(a.getEndDateTime()).thenReturn(end);
        when(a.getState()).thenReturn(state);
        return a;
    }
}
