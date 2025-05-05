package com.martinez.dentist.appointments.services;

import com.martinez.dentist.appointments.models.Appointment;
import com.martinez.dentist.appointments.models.AppointmentState;
import com.martinez.dentist.appointments.repositories.AppointmentRepository;
import com.martinez.dentist.notifications.services.WhatsAppService;
import com.martinez.dentist.patients.models.Patient;
import com.martinez.dentist.patients.repositories.PatientRepository;
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

    @Scheduled(fixedRate = 60000)
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
                                "✅ Confirmar: https://tusitio.com/api/appointments/confirmar/%d\n" +
                                "❌ Cancelar: https://tusitio.com/api/appointments/cancelar/%d",
                        paciente.getFullName(),
                        dia,
                        fechaTurno.toLocalTime().toString(),
                        turno.getProfessional().getFullName(),
                        turno.getId(),
                        turno.getId()
                );

                System.out.println("📨 Enviando mensaje a " + telefono);
                whatsappService.enviarMensaje(telefono, mensaje);

                turno.setReminderSent(true);
                appointmentRepository.save(turno);
                System.out.println("✅ Recordatorio enviado para turno ID: " + turno.getId());
            }
        }
    }
}

