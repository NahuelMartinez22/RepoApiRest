package com.martinez.dentist.appointments.services;

import com.martinez.dentist.appointments.controllers.AppointmentRequestDTO;
import com.martinez.dentist.appointments.controllers.AppointmentResponseDTO;
import com.martinez.dentist.appointments.models.Appointment;
import com.martinez.dentist.appointments.models.AppointmentState;
import com.martinez.dentist.appointments.repositories.AppointmentRepository;
import com.martinez.dentist.exceptions.NoChangesDetectedException;
import com.martinez.dentist.javamail.EmailDTO;
import com.martinez.dentist.javamail.EmailService;
import com.martinez.dentist.patients.controllers.PatientResponseDTO;
import com.martinez.dentist.patients.models.Patient;
import com.martinez.dentist.patients.repositories.PatientRepository;
import com.martinez.dentist.practices.models.Practice;
import com.martinez.dentist.practices.repositories.PracticeRepository;
import com.martinez.dentist.professionals.models.Professional;
import com.martinez.dentist.professionals.models.ProfessionalState;
import com.martinez.dentist.professionals.repositories.ProfessionalRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppointmentService  {

    @Value("${app.notifications.email.appointment-scheduled.enabled}")
    private boolean appointmentScheduledEmailEnabled;

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final ProfessionalRepository professionalRepository;
    private final EmailService emailService;
    private final PracticeRepository practiceRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository,
                              ProfessionalRepository professionalRepository,
                              EmailService emailService,
                              PracticeRepository practiceRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.professionalRepository = professionalRepository;
        this.emailService = emailService;
        this.practiceRepository = practiceRepository;
    }

    public Long createAppointment(AppointmentRequestDTO appointmentRequest) {
        Professional professional = professionalRepository.findById(appointmentRequest.getProfessionalId())
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));

        if (professional.getProfessionalState() != ProfessionalState.ACTIVE) {
            throw new RuntimeException("El profesional seleccionado no está activo.");
        }

        if (!professional.trabajaEsteDiaYHorario(appointmentRequest.getDateTime())) {
            throw new RuntimeException("El profesional no atiende en el día y horario seleccionado.");
        }

        Patient patient = patientRepository.findByDocumentNumber(appointmentRequest.getPatientDni())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        if (appointmentRepository.existsByProfessionalIdAndDateTime(professional.getId(), appointmentRequest.getDateTime())) {
            throw new RuntimeException("El profesional ya tiene un turno asignado en ese horario.");
        }

        if (appointmentRepository.existsByPatientDniAndDateTime(patient.getDocumentNumber(), appointmentRequest.getDateTime())) {
            throw new RuntimeException("El paciente ya tiene un turno asignado en ese horario.");
        }

        // TODO: Hacer desarrollo para recibir el id de la practica. Tarea en el board #29
        Practice practice = practiceRepository.findById(1L).orElseThrow(() -> new RuntimeException("Practica no encontrada"));

        Appointment appointment = new Appointment(
                appointmentRequest.getPatientDni(),
                appointmentRequest.getDateTime(),
                professional,
                appointmentRequest.getReason(),
                appointmentRequest.getState(),
                patient,
                practice
        );

        appointment.setCancelToken(UUID.randomUUID().toString());
        appointment.setConfirmToken(UUID.randomUUID().toString());

        Appointment saved = appointmentRepository.save(appointment);

        if (appointmentScheduledEmailEnabled) {
            notifyAppointmentBooked(patient, appointment, practice);
        }

        return saved.getId();
    }

    public Long updateAppointment(Long id, AppointmentRequestDTO dto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        Professional professional = professionalRepository.findById(dto.getProfessionalId())
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));

        Patient patient = patientRepository.findByDocumentNumber(dto.getPatientDni())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        boolean noChanges =
                Objects.equals(appointment.getPatientDni(), dto.getPatientDni()) &&
                        Objects.equals(appointment.getDateTime(), dto.getDateTime()) &&
                        Objects.equals(appointment.getProfessional().getId(), professional.getId()) &&
                        Objects.equals(appointment.getReason(), dto.getReason()) &&
                        Objects.equals(appointment.getState(), dto.getState()) &&
                        Objects.equals(patient.getNote(), dto.getNote());

        if (noChanges) {
            throw new NoChangesDetectedException("No se detectaron cambios en los datos del turno.");
        }

        if (dto.getNote() != null && !Objects.equals(patient.getNote(), dto.getNote())) {
            patient.setNote(dto.getNote());
            patientRepository.save(patient);
        }

        appointment.updateData(
                dto.getPatientDni(),
                dto.getDateTime(),
                professional,
                dto.getReason(),
                dto.getState(),
                patient
        );

        Appointment saved = appointmentRepository.save(appointment);
        return saved.getId();
    }

    public List<AppointmentResponseDTO> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .map(this::toResponseDTO)
                .toList();
    }

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


    public List<AppointmentResponseDTO> findAppointmentsByDni(String dni) {
        List<Appointment> appointments = appointmentRepository.findByPatientDni(dni);

        return appointments.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<AppointmentResponseDTO> getAppointmentsByProfessionalDni(String dni) {
        List<Appointment> appointments = appointmentRepository.findByProfessionalDocumentNumber(dni);

        return appointments.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public String deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        appointmentRepository.delete(appointment);
        return "Turno eliminado correctamente.";
    }

    public String confirmAppointment(Long id) {
        Appointment turno = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));
        turno.updateState(AppointmentState.CONFIRMADO);
        appointmentRepository.save(turno);
        return "Tu turno fue confirmado con éxito!";
    }

    public String cancelAppointment(Long id) {
        Appointment turno = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));
        turno.updateState(AppointmentState.CANCELADO);
        appointmentRepository.save(turno);
        return "Tu turno fue cancelado correctamente.";
    }

    public void markAsAttended(Long appointmentId, String credentialToken) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        Patient patient = patientRepository.findByDocumentNumber(appointment.getPatientDni())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        if (appointment.getState() != AppointmentState.PENDIENTE) {
            throw new IllegalStateException("El turno ya fue atendido o cancelado.");
        }

        if (patient.getHealthInsurance() != null) {
            if (credentialToken == null || credentialToken.isBlank()) {
                throw new IllegalArgumentException("El token es obligatorio para pacientes con obra social.");
            }
            appointment.registrarCredentialToken(credentialToken);
        } else {
            appointment.registrarCredentialToken(null);
        }

        appointment.setState(AppointmentState.ATENDIDO);
        appointmentRepository.save(appointment);
    }

    public boolean confirmByToken(String token) {
        Optional<Appointment> optional = appointmentRepository.findByConfirmToken(token);
        if (optional.isPresent()) {
            Appointment appointment = optional.get();
            if (appointment.getState() == AppointmentState.CONFIRMADO) return false;

            appointment.setState(AppointmentState.CONFIRMADO);
            appointmentRepository.save(appointment);
            return true;
        }
        return false;
    }

    public boolean cancelByToken(String token) {
        Optional<Appointment> optional = appointmentRepository.findByCancelToken(token);
        if (optional.isPresent()) {
            Appointment appointment = optional.get();
            if (appointment.getState() == AppointmentState.CANCELADO) return false;

            appointment.setState(AppointmentState.CANCELADO);
            appointmentRepository.save(appointment);
            return true;
        }
        return false;
    }

    public List<AppointmentResponseDTO> getAppointmentsByDay(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<Appointment> appointments = appointmentRepository.findAllByDateTimeBetween(startOfDay, endOfDay);

        return appointments.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private AppointmentResponseDTO toResponseDTO(Appointment appointment) {
        Patient patient = patientRepository.findByDocumentNumber(appointment.getPatientDni())
                .orElse(null);
        PatientResponseDTO patientDTO = (patient != null) ? convertToPatientResponseDTO(patient) : null;

        return new AppointmentResponseDTO(
                appointment.getId(),
                patient != null ? patient.getId() : null,
                patientDTO,
                appointment.getDateTime(),
                appointment.getProfessional().getFullName(),
                appointment.getReason(),
                patient != null ? patient.getNote() : null,
                appointment.getState().name()
        );
    }

    private PatientResponseDTO convertToPatientResponseDTO(Patient patient) {
        return new PatientResponseDTO(
                patient.getId(),
                patient.getFullName(),
                patient.getDocumentType(),
                patient.getDocumentNumber(),

                patient.getHealthInsurance() != null ? patient.getHealthInsurance().getId() : null,
                patient.getHealthInsurance() != null ? patient.getHealthInsurance().getName() : null,

                patient.getInsurancePlan() != null ? patient.getInsurancePlan().getId() : null,
                patient.getInsurancePlan() != null ? patient.getInsurancePlan().getName() : null,

                patient.getAffiliateNumber(),
                patient.getPhone(),
                patient.getEmail(),
                patient.getRegistrationDate(),
                patient.getLastVisitDate(),
                patient.getNote(),
                patient.getPatientState().getDisplayName(),
                patient.getIsGuest()
        );
    }

    private void notifyAppointmentBooked(Patient patient, Appointment appointment, Practice practice) {
        if (patient.getEmail() == null || patient.getEmail().isBlank()) {
            return;
        }

        String hour = appointment.getDateTime().toLocalTime().toString();
        String date = appointment.getDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        String body = String.format(getTemplate(),
                patient.getFullName(),
                date,
                hour,
                appointment.getProfessional().getFullName(),
                practice.getName()
        );

        EmailDTO email = new EmailDTO(
                patient.getEmail(),
                "Turno agendado con " + appointment.getProfessional().getFullName(),
                body
        );

        emailService.send(email);
    }

    private String getTemplate() {
        return """
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 0; background-color: #f5f5f5;">
               \s
                <div style="background-color: #1a73e8; padding: 30px; text-align: center; border-radius: 10px 10px 0 0;">
                    <h1 style="color: white; margin: 0; font-size: 24px;">Tu <span style="background-color: rgba(255,255,0,0.4); padding: 0 4px;">turno</span> fue agendado</h1>
                </div>
               \s
                <div style="background-color: white; padding: 30px; text-align: center;">
                    <p style="font-weight: bold; color: #333; font-size: 16px; margin: 0;">%s,</p>
                    <p style="color: #555; margin: 5px 0 20px 0;">Te informamos los datos de tu <span style="background-color: rgba(255,255,0,0.4); padding: 0 4px;">turno</span> agendado:</p>
                   \s
                    <div style="border: 1px solid #e0e0e0; border-radius: 10px; padding: 20px; margin: 10px 0;">
                        <p style="font-size: 28px; margin: 0;">📅</p>
                        <p style="font-weight: bold; font-size: 18px; color: #1a73e8; margin: 5px 0;">%s</p>
                    </div>
                   \s
                    <div style="border: 1px solid #e0e0e0; border-radius: 10px; padding: 20px; margin: 10px 0;">
                        <p style="font-size: 28px; margin: 0;">🕐</p>
                        <p style="font-weight: bold; font-size: 18px; color: #1a73e8; margin: 5px 0;">%s</p>
                    </div>
                   \s
                    <div style="border: 1px solid #e0e0e0; border-radius: 10px; padding: 20px; margin: 10px 0;">
                        <p style="font-size: 28px; margin: 0;">👨‍⚕️</p>
                        <p style="font-weight: bold; font-size: 18px; color: #1a73e8; margin: 5px 0;">%s</p>
                    </div>
                   \s
                    <div style="border: 1px solid #e0e0e0; border-radius: 10px; padding: 20px; margin: 10px 0;">
                        <p style="font-size: 28px; margin: 0;">🏥</p>
                        <p style="font-weight: bold; font-size: 18px; color: #1a73e8; margin: 5px 0;">%s</p>
                    </div>
                   \s
                    <p style="color: #888; margin-top: 30px; font-size: 14px;">¡Gracias por confiar en nosotros!</p>
                </div>
               \s
                <div style="background-color: #1a73e8; padding: 15px; text-align: center; border-radius: 0 0 10px 10px;">
                    <p style="color: white; margin: 0; font-size: 12px;">OdontoTurno © 2026</p>
                </div>
               \s
            </div>
           \s""";
    }
}
