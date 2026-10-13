package com.martinez.dentist.professionals.models;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProfessionalScheduleRulesTest {

    // Miércoles 14/10/2026, atiende de 9 a 13
    private static final LocalDateTime MIERCOLES = LocalDateTime.of(2026, 10, 14, 0, 0);

    private Professional profesional() {
        Professional p = new Professional();
        ReflectionTestUtils.setField(p, "schedules", List.of(
                new ProfessionalSchedule(p, DayOfWeek.WEDNESDAY, LocalTime.of(9, 0), LocalTime.of(13, 0))));
        return p;
    }

    @Test
    void aceptaTurnosQueEntranEnElHorario() {
        Professional p = profesional();
        assertThat(p.trabajaEsteDiaYHorario(MIERCOLES.withHour(9), MIERCOLES.withHour(10))).isTrue();
        assertThat(p.trabajaEsteDiaYHorario(MIERCOLES.withHour(12).withMinute(30), MIERCOLES.withHour(13))).isTrue();
        // Como hasta ahora: un turno de 15 minutos que empieza a la hora de cierre
        assertThat(p.trabajaEsteDiaYHorario(MIERCOLES.withHour(13), MIERCOLES.withHour(13).withMinute(15))).isTrue();
    }

    @Test
    void rechazaTurnosQueSePasanDelHorarioOEnOtroDia() {
        Professional p = profesional();
        assertThat(p.trabajaEsteDiaYHorario(MIERCOLES.withHour(12).withMinute(30), MIERCOLES.withHour(13).withMinute(30))).isFalse();
        assertThat(p.trabajaEsteDiaYHorario(MIERCOLES.withHour(8).withMinute(45), MIERCOLES.withHour(9).withMinute(15))).isFalse();
        assertThat(p.trabajaEsteDiaYHorario(MIERCOLES.plusDays(1).withHour(9), MIERCOLES.plusDays(1).withHour(10))).isFalse();
    }

    @Test
    void detectaLicencia() {
        Professional p = profesional();
        p.setLicenseStartDate(LocalDate.of(2026, 10, 19));
        p.setLicenseEndDate(LocalDate.of(2026, 10, 23));

        assertThat(p.estaDeLicencia(LocalDate.of(2026, 10, 19))).isTrue();
        assertThat(p.estaDeLicencia(LocalDate.of(2026, 10, 23))).isTrue();
        assertThat(p.estaDeLicencia(LocalDate.of(2026, 10, 24))).isFalse();
        assertThat(new Professional().estaDeLicencia(LocalDate.of(2026, 10, 20))).isFalse();
    }
}
