package com.martinez.dentist.appointments.repositories;

import com.martinez.dentist.appointments.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientDni(String dni);

    List<Appointment> findByProfessionalDocumentNumber(String dni);

    boolean existsByProfessionalIdAndDateTime(Long professionalId, LocalDateTime dateTime);

    boolean existsByPatientDniAndDateTime(String patientDni, LocalDateTime dateTime);


    @Query("SELECT DATE(a.dateTime), COUNT(a) " +
            "FROM Appointment a " +
            "WHERE a.dateTime >= :desde " +
            "GROUP BY DATE(a.dateTime)")
    List<Object[]> countAppointmentsByDayLastWeek(@Param("desde") LocalDateTime desde);

    @Query("SELECT COUNT(a) FROM Appointment a " +
            "WHERE a.dateTime >= :desde " +
            "AND a.state = com.martinez.dentist.appointments.models.AppointmentState.ATENDIDO")
    long countAttendedAppointmentsLast30Days(@Param("desde") LocalDateTime desde);
    @Query("SELECT a.state, COUNT(a) " +
            "FROM Appointment a " +
            "WHERE a.dateTime >= :desde " +
            "GROUP BY a.state")
    List<Object[]> countAppointmentsByStateSince(@Param("desde") LocalDateTime desde);
}
