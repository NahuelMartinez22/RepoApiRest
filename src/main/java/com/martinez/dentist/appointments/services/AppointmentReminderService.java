package com.martinez.dentist.appointments.services;

import com.martinez.dentist.appointments.models.Appointment;
import com.martinez.dentist.appointments.models.AppointmentState;
import com.martinez.dentist.appointments.repositories.AppointmentRepository;
import com.martinez.dentist.patients.models.Patient;
import com.martinez.dentist.patients.repositories.PatientRepository;
import com.martinez.dentist.javamail.EmailDTO;
import com.martinez.dentist.javamail.EmailService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentReminderService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final EmailService emailService;

    public AppointmentReminderService(AppointmentRepository appointmentRepository, PatientRepository patientRepository, EmailService emailService) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.emailService = emailService;
    }

    public void enviarRecordatoriosAutomaticamente() {
        int enviados = processRecordatorios();
        System.out.println("🔁 Recordatorios automáticos enviados: " + enviados);
    }

    public int enviarRecordatoriosManualmente() {
        return processRecordatorios();
    }

    private int processRecordatorios() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime en24Horas = ahora.plusHours(24);

        System.out.println("⏰ Ahora: " + ahora);
        System.out.println("🔔 Enviando recordatorios para turnos entre ahora y las próximas 24hs...");

        List<Appointment> proximosTurnos = appointmentRepository.findByState(AppointmentState.PENDIENTE);
        int enviados = 0;

        for (Appointment turno : proximosTurnos) {
            if (turno.isReminderSent()) continue;

            LocalDateTime fechaTurno = turno.getDateTime();
            if (fechaTurno.isAfter(ahora) && fechaTurno.isBefore(en24Horas)) {
                Patient paciente = patientRepository.findByDocumentNumber(turno.getPatientDni()).orElse(null);
                if (paciente == null || paciente.getPhone() == null) continue;

                String telefono = paciente.getPhone().startsWith("+")
                        ? paciente.getPhone()
                        : "+" + paciente.getPhone();

                String dia = fechaTurno.getDayOfWeek()
                        .getDisplayName(java.time.format.TextStyle.FULL, new java.util.Locale("es", "ES"));

                String mensaje = String.format(
                        "Hola %s, recordatorio de tu turno el %s a las %s con %s.\n\n" +
                                "✅ Confirmar: https://odonto-turno.up.railway.app/appointments/confirm/%s\n" +
                                "❌ Cancelar: https://odonto-turno.up.railway.app/appointments/cancel/%s",
                        paciente.getFullName(),
                        dia,
                        fechaTurno.toLocalTime().toString(),
                        turno.getProfessional().getFullName(),
                        turno.getConfirmToken(),
                        turno.getCancelToken()
                );

                try {
                    System.out.println("📨 Enviando mensaje a " + telefono);
                } catch (Exception e) {
                    System.out.println("⚠️ Error al enviar WhatsApp: " + e.getMessage());
                }

                try {
                    if (paciente.getEmail() != null && !paciente.getEmail().isBlank()) {
                        EmailDTO email = new EmailDTO(
                                paciente.getEmail(),
                                "Recordatorio de turno",
                                mensaje
                        );
                        emailService.send( email);
                        System.out.println("📧 Correo enviado a " + paciente.getEmail());
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ Error al enviar correo: " + e.getMessage());
                }

                turno.setReminderSent(true);
                appointmentRepository.save(turno);
                System.out.println("✅ Recordatorio enviado para turno ID: " + turno.getId());
                enviados++;
            }
        }

        return enviados;
    }
}
