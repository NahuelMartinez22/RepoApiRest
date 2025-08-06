package com.martinez.dentist.appointments.services;

import com.martinez.dentist.appointments.models.Appointment;
import com.martinez.dentist.appointments.models.AppointmentState;
import com.martinez.dentist.appointments.repositories.AppointmentRepository;
import com.martinez.dentist.notifications.services.WhatsAppService;
import com.martinez.dentist.patients.models.Patient;
import com.martinez.dentist.patients.repositories.PatientRepository;
import com.martinez.dentist.javamail.EmailDTO;
import com.martinez.dentist.javamail.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentReminderService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private WhatsAppService whatsappService;

    @Scheduled(fixedRate = 5000)//30000 = 30s ----- 86400000 = 24hs
    public void enviarRecordatorios() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime en24Horas = ahora.plusHours(24);

        System.out.println("⏰ Ahora: " + ahora);
        System.out.println("🔔 Enviando recordatorios para turnos entre ahora y las próximas 24hs...");

        List<Appointment> proximosTurnos = appointmentRepository.findByState(AppointmentState.PENDIENTE);

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

                // Enviar WhatsApp con manejo de error
                try {
                    System.out.println("📨 Enviando mensaje a " + telefono);
                    whatsappService.enviarMensaje(telefono, mensaje);
                } catch (Exception e) {
                    System.out.println("⚠️ Error al enviar WhatsApp: " + e.getMessage());
                    e.printStackTrace(); // Opcional, para ver más detalles del error
                }

                // Enviar correo electrónico
                try {
                    if (paciente.getEmail() != null && !paciente.getEmail().isBlank()) {
                        EmailDTO email = new EmailDTO(
                                paciente.getEmail(),
                                "Recordatorio de turno",
                                mensaje
                        );
                        EmailService.enviar(email);
                        System.out.println("📧 Correo enviado a " + paciente.getEmail());
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ Error al enviar correo: " + e.getMessage());
                    e.printStackTrace();
                }

                // Marcar turno como recordado
                turno.setReminderSent(true);
                appointmentRepository.save(turno);
                System.out.println("✅ Recordatorio enviado para turno ID: " + turno.getId());
            }
        }
    }
}