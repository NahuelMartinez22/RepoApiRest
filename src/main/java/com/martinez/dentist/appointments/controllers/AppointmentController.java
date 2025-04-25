package com.martinez.dentist.appointments.controllers;

import com.martinez.dentist.appointments.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
