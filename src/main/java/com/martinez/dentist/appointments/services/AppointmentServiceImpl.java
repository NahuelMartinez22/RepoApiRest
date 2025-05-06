package com.martinez.dentist.appointments.services;

import com.martinez.dentist.appointments.controllers.AppointmentRequestDTO;
import com.martinez.dentist.appointments.controllers.AppointmentResponseDTO;
import com.martinez.dentist.appointments.models.Appointment;
import com.martinez.dentist.appointments.models.AppointmentState;
import com.martinez.dentist.appointments.repositories.AppointmentRepository;
import com.martinez.dentist.javamail.EmailDTO;
import com.martinez.dentist.javamail.EmailService;
import com.martinez.dentist.patients.controllers.PatientResponseDTO;
import com.martinez.dentist.patients.models.Patient;
import com.martinez.dentist.patients.repositories.PatientRepository;
import com.martinez.dentist.professionals.models.Professional;
import com.martinez.dentist.professionals.models.ProfessionalState;
import com.martinez.dentist.professionals.repositories.ProfessionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ProfessionalRepository professionalRepository;

    @Override
    public String createAppointment(AppointmentRequestDTO dto) {
        Professional professional = professionalRepository.findById(dto.getProfessionalId())
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));

        if (professional.getProfessionalState() != ProfessionalState.ACTIVE) {
            throw new RuntimeException("El profesional seleccionado no está activo.");
        }

        if (!professional.trabajaEsteDiaYHorario(dto.getDateTime())) {
            throw new RuntimeException("El profesional no atiende en el día y horario seleccionado.");
        }

        Patient patient = patientRepository.findByDocumentNumber(dto.getPatientDni())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        if (dto.getDateTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No se puede crear un turno en una fecha/hora que ya pasó.");
        }

        if (appointmentRepository.existsByProfessionalIdAndDateTime(professional.getId(), dto.getDateTime())) {
            throw new RuntimeException("El profesional ya tiene un turno asignado en ese horario.");
        }

        if (appointmentRepository.existsByPatientDniAndDateTime(patient.getDocumentNumber(), dto.getDateTime())) {
            throw new RuntimeException("El paciente ya tiene un turno asignado en ese horario.");
        }

        Appointment appointment = new Appointment(
                dto.getPatientDni(),
                dto.getDateTime(),
                professional,
                dto.getReason(),
                dto.getState()
        );

        appointmentRepository.save(appointment);

        try {
            if (patient.getEmail() != null && !patient.getEmail().isBlank()) {
                String dia = dto.getDateTime().getDayOfWeek()
                        .getDisplayName(java.time.format.TextStyle.FULL, new java.util.Locale("es", "ES"));

                String hora = dto.getDateTime().toLocalTime().toString();

                String cuerpo = String.format(
                        "Hola %s,\n\nTu turno fue creado con éxito. Te esperamos el %s a las %s con el profesional %s.\n" +
                                "Dirección: Av. Corrientes 3822, CABA.\nMotivo: %s\n\n¡Gracias por confiar en nosotros!",
                        patient.getFullName(),
                        dia,
                        hora,
                        professional.getFullName(),
                        dto.getReason()
                );

                EmailDTO email = new EmailDTO(
                        patient.getEmail(),
                        "Confirmación de turno",
                        cuerpo
                );

                EmailService.enviar(email);
                System.out.println("📧 Correo de confirmación enviado a " + patient.getEmail());
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error al enviar el correo de confirmación: " + e.getMessage());
        }

        return "Turno creado con éxito.";
    }


    @Override
    public List<AppointmentResponseDTO> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();

        return appointments.stream().map(appointment -> {
            Patient patient = patientRepository.findByDocumentNumber(appointment.getPatientDni())
                    .orElse(null);
            PatientResponseDTO patientDTO = (patient != null) ? convertToPatientResponseDTO(patient) : null;

            String professionalFullName = appointment.getProfessional().getFullName();

            return new AppointmentResponseDTO(
                    appointment.getId(),
                    patientDTO,
                    appointment.getDateTime(),
                    professionalFullName,
                    appointment.getReason(),
                    appointment.getState().name()
            );
        }).toList();
    }

    @Override
    public String updateAppointmentState(Long id, String state) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        try {
            AppointmentState newState = AppointmentState.valueOf(state.toUpperCase());
            appointment.updateState(newState);

            if (newState == AppointmentState.ATENDIDO) {
                Patient patient = patientRepository.findByDocumentNumber(appointment.getPatientDni())
                        .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

                patient.setLastVisitDate(LocalDate.now());
                patientRepository.save(patient);
            }

            appointmentRepository.save(appointment);
            return "Estado del turno actualizado a: " + newState.name();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado inválido: " + state);
        }
    }

    @Override
    public String updateAppointment(Long id, AppointmentRequestDTO dto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        Professional professional = professionalRepository.findById(dto.getProfessionalId())
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));

        Patient patient = patientRepository.findByDocumentNumber(dto.getPatientDni())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        if (dto.getDateTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No se puede asignar un turno en una fecha/hora pasada.");
        }

        appointment.updateData(
                dto.getPatientDni(),
                dto.getDateTime(),
                professional,
                dto.getReason(),
                dto.getState()
        );

        appointmentRepository.save(appointment);
        return "Turno actualizado correctamente.";
    }

    @Override
    public List<AppointmentResponseDTO> findAppointmentsByDni(String dni) {
        List<Appointment> appointments = appointmentRepository.findByPatientDni(dni);

        if (appointments.isEmpty()) {
            throw new RuntimeException("No se encontraron turnos con el DNI: " + dni);
        }

        return appointments.stream().map(appointment -> {
            Patient patient = patientRepository.findByDocumentNumber(appointment.getPatientDni())
                    .orElse(null);
            PatientResponseDTO patientDTO = (patient != null) ? convertToPatientResponseDTO(patient) : null;

            String professionalFullName = appointment.getProfessional().getFullName();

            return new AppointmentResponseDTO(
                    appointment.getId(),
                    patientDTO,
                    appointment.getDateTime(),
                    professionalFullName,
                    appointment.getReason(),
                    appointment.getState().name()
            );
        }).toList();
    }

    @Override
    public List<AppointmentResponseDTO> getAppointmentsByProfessionalDni(String dni) {
        List<Appointment> appointments = appointmentRepository.findByProfessionalDocumentNumber(dni);

        return appointments.stream().map(appointment -> {
            Patient patient = patientRepository.findByDocumentNumber(appointment.getPatientDni())
                    .orElse(null);
            PatientResponseDTO patientDTO = (patient != null) ? convertToPatientResponseDTO(patient) : null;

            String professionalFullName = appointment.getProfessional().getFullName();

            return new AppointmentResponseDTO(
                    appointment.getId(),
                    patientDTO,
                    appointment.getDateTime(),
                    professionalFullName,
                    appointment.getReason(),
                    appointment.getState().name()
            );
        }).toList();
    }


    private PatientResponseDTO convertToPatientResponseDTO(Patient patient) {
        return new PatientResponseDTO(
                patient.getId(),
                patient.getFullName(),
                patient.getDocumentType(),
                patient.getDocumentNumber(),
                patient.getHealthInsurance(),
                patient.getInsurancePlan(),
                patient.getPhone(),
                patient.getEmail(),
                patient.getRegistrationDate(),
                patient.getLastVisitDate(),
                patient.getNote(),
                patient.getPatientState().name()
        );
    }

    @Override
    public String deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        appointmentRepository.delete(appointment);
        return "Turno eliminado correctamente.";
    }

    @Override
    public String confirmAppointment(Long id) {
        Appointment turno = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));
        turno.updateState(AppointmentState.CONFIRMADO);
        appointmentRepository.save(turno);
        return "✅ ¡Tu turno fue confirmado con éxito!";
    }

    @Override
    public String cancelAppointment(Long id) {
        Appointment turno = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));
        turno.updateState(AppointmentState.CANCELADO);
        appointmentRepository.save(turno);
        return "❌ Tu turno fue cancelado correctamente.";
    }

}
