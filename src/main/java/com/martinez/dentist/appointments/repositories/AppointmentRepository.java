package com.martinez.dentist.appointments.repositories;

import com.martinez.dentist.appointments.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}