package com.martinez.dentist.appointments.repositories;

import com.martinez.dentist.appointments.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientDni(String dni);
    List<Appointment> findByProfessionalDocumentNumber(String dni);
}