package com.martinez.dentist.appointments.controllers;

import com.martinez.dentist.appointments.models.Appointment;
import com.martinez.dentist.appointments.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> getAppointment(@PathVariable Long id) {
        AppointmentResponseDTO dto = appointmentService.getAppointmentWithDetails(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointments() {
        List<AppointmentResponseDTO> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }


    @PostMapping
    public ResponseEntity<String> createAppointment(@RequestBody AppointmentRequestDTO request) {
        String error = appointmentService.createAppointment(request);
        if (error != null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Turno creado correctamente");
    }

}
