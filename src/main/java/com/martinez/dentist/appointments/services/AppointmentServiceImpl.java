package com.martinez.dentist.appointments.services;

import com.martinez.dentist.appointments.controllers.AppointmentRequestDTO;
import com.martinez.dentist.appointments.controllers.AppointmentResponseDTO;
import com.martinez.dentist.appointments.controllers.BillingAppointmentDTO;
import com.martinez.dentist.appointments.models.Appointment;
import com.martinez.dentist.appointments.models.AppointmentState;
import com.martinez.dentist.appointments.repositories.AppointmentRepository;
import com.martinez.dentist.exceptions.NoChangesDetectedException;
import com.martinez.dentist.javamail.EmailDTO;
import com.martinez.dentist.javamail.EmailService;
import com.martinez.dentist.patients.controllers.PatientResponseDTO;
import com.martinez.dentist.patients.models.Patient;
import com.martinez.dentist.patients.repositories.DentalProcedureRepository;
import com.martinez.dentist.patients.repositories.PatientRepository;
import com.martinez.dentist.professionals.models.Professional;
import com.martinez.dentist.professionals.models.ProfessionalState;
import com.martinez.dentist.professionals.repositories.ProfessionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ProfessionalRepository professionalRepository;

    @Autowired
    private DentalProcedureRepository dentalProcedureRepository;

    @Override
    public Long createAppointment(AppointmentRequestDTO dto) {
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

        appointment.setCancelToken(UUID.randomUUID().toString());
        appointment.setConfirmToken(UUID.randomUUID().toString());

        Appointment saved = appointmentRepository.save(appointment);

        if (patient.getEmail() != null && !patient.getEmail().isBlank()) {
            String dia = dto.getDateTime().getDayOfWeek()
                    .getDisplayName(java.time.format.TextStyle.FULL, new java.util.Locale("es", "ES"));
            String hora = dto.getDateTime().toLocalTime().toString();

            String cancelUrl = "https://tusitio.com/appointments/cancel/" + appointment.getCancelToken();
            String confirmUrl = "https://tusitio.com/appointments/confirm/" + appointment.getConfirmToken();

            String cuerpo = String.format(
                    "Hola %s,\n\nTu turno fue creado con éxito. Te esperamos el %s a las %s con el profesional %s.\n" +
                            "Dirección: Av. Corrientes 3822, CABA.\nMotivo: %s\n\n" +
                            "✔️ Confirmar turno: %s\n" +
                            "❌ Cancelar turno: %s\n\n" +
                            "¡Gracias por confiar en nosotros!",
                    patient.getFullName(),
                    dia,
                    hora,
                    professional.getFullName(),
                    dto.getReason(),
                    confirmUrl,
                    cancelUrl
            );

            EmailDTO email = new EmailDTO(
                    patient.getEmail(),
                    "Confirmación de turno",
                    cuerpo
            );

            new Thread(() -> {
                try {
                    EmailService.enviar(email);
                } catch (Exception e) {
                    // Log o manejo del error
                }
            }).start();
        }

        return saved.getId();
    }

    @Override
    public Long updateAppointment(Long id, AppointmentRequestDTO dto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        Professional professional = professionalRepository.findById(dto.getProfessionalId())
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));

        Patient patient = patientRepository.findByDocumentNumber(dto.getPatientDni())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        if (dto.getDateTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No se puede asignar un turno en una fecha/hora pasada.");
        }

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
                dto.getState()
        );

        Appointment saved = appointmentRepository.save(appointment);
        return saved.getId();
    }



    @Override
    public List<AppointmentResponseDTO> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .map(this::toResponseDTO)
                .toList();
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
    public List<AppointmentResponseDTO> findAppointmentsByDni(String dni) {
        List<Appointment> appointments = appointmentRepository.findByPatientDni(dni);

        if (appointments.isEmpty()) {
            throw new RuntimeException("No se encontraron turnos con el DNI: " + dni);
        }

        return appointments.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Override
    public List<AppointmentResponseDTO> getAppointmentsByProfessionalDni(String dni) {
        List<Appointment> appointments = appointmentRepository.findByProfessionalDocumentNumber(dni);

        return appointments.stream()
                .map(this::toResponseDTO)
                .toList();
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
                patient.getPatientState().getDisplayName()
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
        return "Tu turno fue confirmado con éxito!";
    }

    @Override
    public String cancelAppointment(Long id) {
        Appointment turno = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));
        turno.updateState(AppointmentState.CANCELADO);
        appointmentRepository.save(turno);
        return "Tu turno fue cancelado correctamente.";
    }


    @Override
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

    @Override
    public Map<String, Object> getAppointmentsForBilling(Long obraSocialId, int mes, int anio) {
        List<Appointment> appointments = appointmentRepository.findAppointmentsForBilling(obraSocialId, mes, anio);

        List<BillingAppointmentDTO> dtoList = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (Appointment appt : appointments) {
            Patient patient = patientRepository.findByDocumentNumber(appt.getPatientDni()).orElse(null);
            if (patient == null) continue;

            String nombreApellido = patient.getFullName();
            String dni = patient.getDocumentNumber();
            String numeroAfiliado = patient.getAffiliateNumber();
            String plan = patient.getInsurancePlan().getName();
            String token = appt.getCredentialToken();

            dtoList.add(new BillingAppointmentDTO(
                    appt.getDateTime().toLocalDate(),
                    nombreApellido,
                    dni,
                    numeroAfiliado,
                    plan,
                    "Sin procedimiento",
                    0.0,
                    token
            ));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("turnos", dtoList);
        response.put("subtotal", subtotal.doubleValue());
        return response;
    }

    @Override
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

    @Override
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

    @Override
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


}
