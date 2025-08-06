package com.martinez.dentist.appointments.controllers;

import com.martinez.dentist.appointments.services.AppointmentReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentReminderController {

    @Autowired
    private AppointmentReminderService reminderService;

    @GetMapping("/enviar-recordatorios")
    public Map<String, Object> enviarRecordatoriosManualmente() {
        int cantidad = reminderService.enviarRecordatoriosManualmente();

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Recordatorios procesados correctamente.");
        response.put("cantidad", cantidad);
        return response;
    }
}
