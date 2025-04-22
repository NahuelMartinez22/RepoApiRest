package com.martinez.dentist.pacients.controllers;

import com.martinez.dentist.pacients.models.Patient;
import com.martinez.dentist.pacients.models.PatientState;
import com.martinez.dentist.pacients.repositories.PatientServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientServiceManager serviceManager;

    @PostMapping
    @Transactional
    public ResponseEntity<String> save(@RequestBody PatientRequestDTO dto) {
        Optional<Patient> existingPatient = serviceManager.findByDocumentNumber(dto.getDocumentNumber());
        if (existingPatient.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Ya existe un paciente con ese DNI");
        }

        Patient patient = new Patient(
                dto.getFullName(),
                dto.getDocumentType(),
                dto.getDocumentNumber(),
                dto.getHealthInsurance(),
                dto.getInsurancePlan(),
                dto.getPhone(),
                dto.getRegistrationDate(),
                dto.getLastVisitDate(),
                dto.getNote()
        );

        serviceManager.save(patient);
        return ResponseEntity.ok("El paciente se creó con éxito.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getByIdPatient(@PathVariable Long id) {

        Patient patient;

        try {
            patient = this.serviceManager.findById(id);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        PatientResponseDTO patientDTO = new PatientResponseDTO(
                patient.getId(),
                patient.getFullName(),
                patient.getDocumentType(),
                patient.getDocumentNumber(),
                patient.getHealthInsurance(),
                patient.getInsurancePlan(),
                patient.getPhone(),
                patient.getRegistrationDate(),
                patient.getLastVisitDate(),
                patient.getNote(),
                patient.getPatientState().getDisplayName()
        );
        return ResponseEntity.ok(patientDTO);
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<PatientResponseDTO>> findAllPatients(@RequestParam(required = false) String state) {
        List<Patient> patients;

        if (state != null) {
            try {
                PatientState parsedState = PatientState.valueOf(state.toUpperCase());
                patients = serviceManager.findByState(parsedState);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(null);
            }
        } else {
            patients = serviceManager.findAll();
        }

        List<PatientResponseDTO> responseList = patients.stream().map(patient -> new PatientResponseDTO(
                patient.getId(),
                patient.getFullName(),
                patient.getDocumentType(),
                patient.getDocumentNumber(),
                patient.getHealthInsurance(),
                patient.getInsurancePlan(),
                patient.getPhone(),
                patient.getRegistrationDate(),
                patient.getLastVisitDate(),
                patient.getNote(),
                patient.getPatientState().getDisplayName()
        )).toList();

        return ResponseEntity.ok(responseList);
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<String> disablePatient(@PathVariable Long id) {
        serviceManager.disablePatient(id);
        return ResponseEntity.ok("Paciente deshabilitado.");
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<String> enablePatient(@PathVariable Long id) {
        serviceManager.enablePatient(id);
        return ResponseEntity.ok("Paciente habilitado.");
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> updatePatient(@PathVariable Long id,
                                           @RequestBody PatientRequestDTO dto) {
        try {
            Patient existing = serviceManager.findById(id);
            existing.updateData(dto);
            serviceManager.save(existing);

            PatientResponseDTO responseDTO = new PatientResponseDTO(
                    existing.getId(),
                    existing.getFullName(),
                    existing.getDocumentType(),
                    existing.getDocumentNumber(),
                    existing.getHealthInsurance(),
                    existing.getInsurancePlan(),
                    existing.getPhone(),
                    existing.getRegistrationDate(),
                    existing.getLastVisitDate(),
                    existing.getNote(),
                    existing.getPatientState().getDisplayName()
            );
            return ResponseEntity.ok(responseDTO);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró el paciente: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar paciente: " + e.getMessage());
        }
    }
}

