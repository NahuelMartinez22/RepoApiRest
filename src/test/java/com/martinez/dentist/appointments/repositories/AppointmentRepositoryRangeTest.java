package com.martinez.dentist.appointments.repositories;

import com.martinez.dentist.appointments.models.Appointment;
import com.martinez.dentist.appointments.models.AppointmentState;
import com.martinez.dentist.patients.models.Patient;
import com.martinez.dentist.practices.models.Practice;
import com.martinez.dentist.professionals.models.Professional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:appointments;MODE=MySQL;DATABASE_TO_LOWER=TRUE",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class AppointmentRepositoryRangeTest {

    @Autowired
    private AppointmentRepository repository;

    @Autowired
    private TestEntityManager em;

    @Test
    void traeLosTurnosDelProfesionalEnElRangoOrdenados() {
        Practice practice = new Practice();
        ReflectionTestUtils.setField(practice, "name", "Consultorio");
        em.persist(practice);
        Professional gomez = em.persist(new Professional("Laura Gomez", "DNI", "111", "1"));
        Professional otro = em.persist(new Professional("Otro", "DNI", "222", "2"));
        Patient ana = em.persist(new Patient("Ana", "DNI", "123", null, null, null, null, null,
                LocalDate.now(), null, null, false));

        LocalDateTime base = LocalDateTime.of(2026, 10, 14, 9, 0);
        turno(ana, gomez, practice, base.plusHours(2));
        turno(ana, gomez, practice, base);
        turno(ana, gomez, practice, base.plusDays(20));
        turno(ana, otro, practice, base.plusHours(1));
        em.flush();

        List<Appointment> result = repository.findByProfessionalIdAndDateTimeBetweenOrderByDateTime(
                gomez.getId(), base.toLocalDate().atStartOfDay(), base.toLocalDate().atTime(23, 59));

        assertThat(result).extracting(Appointment::getDateTime).containsExactly(base, base.plusHours(2));
        assertThat(repository.findByPatientDniAndDateTimeBetween("123",
                base.toLocalDate().atStartOfDay(), base.toLocalDate().atTime(23, 59))).hasSize(3);
    }

    private void turno(Patient patient, Professional professional, Practice practice, LocalDateTime start) {
        em.persist(new Appointment("123", start, start.plusMinutes(30), professional, "Control",
                AppointmentState.PENDIENTE, patient, practice));
    }
}
