package com.martinez.dentist.appointments.services;

import com.martinez.dentist.appointments.controllers.AppointmentRequestDTO;
import com.martinez.dentist.appointments.controllers.AppointmentResponseDTO;
import com.martinez.dentist.appointments.models.Appointment;
import com.martinez.dentist.appointments.models.AppointmentState;
import com.martinez.dentist.appointments.repositories.AppointmentRepository;
import com.martinez.dentist.pacients.models.Patient;
import com.martinez.dentist.pacients.repositories.PatientRepository;
import com.martinez.dentist.users.models.User;
import com.martinez.dentist.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AppointmentIMPL implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    @Autowired
    public AppointmentIMPL(AppointmentRepository appointmentRepository, PatientRepository patientRepository, UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }


    @Override
    public String createAppointment(AppointmentRequestDTO request) {
        User doctor = userRepository.findById(request.getDoctorId()).orElse(null);
        if (doctor == null) return "Doctor no encontrado";

        if (!patientRepository.findByDocumentNumber(request.getPatientDni()).isPresent()) {
            return "Paciente no encontrado";
        }

        Appointment appointment = new Appointment(
                request.getPatientDni(),
                request.getDateTime(),
                doctor,
                request.getReason(),
                AppointmentState.PENDING
        );

        appointmentRepository.save(appointment);
        return null;
    }

    @Override
    public AppointmentResponseDTO getAppointmentWithDetails(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));


        Patient patient = patientRepository.findByDocumentNumber(appointment.getPatientDni())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        String patientFullName = patient.getFullName();
        String doctorFullName = appointment.getDoctor().getUsername();

        return new AppointmentResponseDTO(
                appointment.getPatientDni(),
                patientFullName,
                appointment.getDateTime(),
                doctorFullName,
                appointment.getReason(),
                appointment.getState().name()
        );
    }

    @Override
    public List<AppointmentResponseDTO> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();

        return appointments.stream().map(appointment -> {
            Patient patient = patientRepository.findByDocumentNumber(appointment.getPatientDni()).orElse(null);
            String patientFullName = (patient != null) ? patient.getFullName() : "Paciente desconocido";
            String doctorFullName = appointment.getDoctor().getUsername();

            return new AppointmentResponseDTO(
                    appointment.getPatientDni(),
                    patientFullName,
                    appointment.getDateTime(),
                    doctorFullName,
                    appointment.getReason(),
                    appointment.getState().name()
            );
        }).toList();
    }
}
