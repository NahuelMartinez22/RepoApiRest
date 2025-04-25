package com.martinez.dentist.patients.controllers;

import com.martinez.dentist.patients.models.PatientState;
import com.martinez.dentist.patients.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@PreAuthorize("hasAnyRole('ADMIN', 'MODERADOR')")
public class PatientController {

    @Autowired
    private PatientService service;


    @PostMapping
    @Transactional
    public ResponseEntity<String> save(@RequestBody PatientRequestDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }


    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> getByIdPatient(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }


    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<PatientResponseDTO>> findAllPatients(@RequestParam(required = false) String state) {
        if (state != null) {
            try {
                PatientState parsedState = PatientState.valueOf(state.toUpperCase());
                return ResponseEntity.ok(service.findByState(parsedState));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.ok(service.findAll());
    }


    @PatchMapping("/{id}/disable")
    public ResponseEntity<String> disablePatient(@PathVariable Long id) {
        service.disable(id);
        return ResponseEntity.ok("Paciente deshabilitado.");
    }


    @PatchMapping("/{id}/enable")
    public ResponseEntity<String> enablePatient(@PathVariable Long id) {
        service.enable(id);
        return ResponseEntity.ok("Paciente habilitado.");
    }


    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable Long id,
                                                            @RequestBody PatientRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }
}
