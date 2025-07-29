package com.martinez.dentist.appointments.controllers;

import com.martinez.dentist.appointments.models.Appointment;
import com.martinez.dentist.appointments.models.AppointmentState;
import com.martinez.dentist.appointments.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    @Transactional
    public ResponseEntity<String> createAppointment(@RequestBody AppointmentRequestDTO dto) {
        return ResponseEntity.ok(appointmentService.createAppointment(dto));
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/by-day")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByDay(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDay(date));
    }

    @PatchMapping("/{id}/state")
    @Transactional
    public ResponseEntity<String> updateAppointmentState(@PathVariable Long id,
                                                         @RequestParam String state) {
        return ResponseEntity.ok(appointmentService.updateAppointmentState(id, state));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<String> updateAppointment(@PathVariable Long id,
                                                    @RequestBody AppointmentRequestDTO dto) {
        return ResponseEntity.ok(appointmentService.updateAppointment(id, dto));
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByDni(@PathVariable String dni) {
        return ResponseEntity.ok(appointmentService.findAppointmentsByDni(dni));
    }

    @GetMapping("/professional/dni/{dni}")
    public ResponseEntity<List<AppointmentResponseDTO>> getByProfessionalDni(@PathVariable String dni) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByProfessionalDni(dni));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> deleteAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.deleteAppointment(id));
    }

    @GetMapping("/confirmar/{id}")
    public ResponseEntity<String> confirmAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.confirmAppointment(id));
    }

    @GetMapping("/cancelar/{id}")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(id));
    }

    @PutMapping("/{id}/attend")
    @Transactional
    public ResponseEntity<String> attendAppointment(@PathVariable Long id,
                                                    @RequestBody AttendAppointmentRequestDTO dto) {
        appointmentService.markAsAttended(id, dto.getCredentialToken());
        return ResponseEntity.ok("Turno marcado como ATENDIDO correctamente.");
    }

    @GetMapping("/facturacion")
    public ResponseEntity<Map<String, Object>> getFacturacionMensual(
            @RequestParam Long obraSocialId,
            @RequestParam int mes,
            @RequestParam int anio) {

        return ResponseEntity.ok(appointmentService.getAppointmentsForBilling(obraSocialId, mes, anio));
    }


}
