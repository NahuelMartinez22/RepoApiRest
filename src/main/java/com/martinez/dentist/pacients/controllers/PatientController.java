package com.martinez.dentist.pacients.controllers;

import com.martinez.dentist.pacients.models.Patient;
import com.martinez.dentist.pacients.repositories.PatientServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientServiceManager serviceManager;

    @PostMapping
    @Transactional
    public ResponseEntity<String> save(@RequestBody PatientRequestDTO dto) {
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

        PatientResponseDTO patientDTO = new PatientResponseDTO(patient.getId(),
                patient.getFullName(), patient.getDocumentType(), patient.getDocumentNumber(),
                patient.getHealthInsurance(), patient.getInsurancePlan(), patient.getPhone(),
                patient.getRegistrationDate(),patient.getLastVisitDate(), patient.getNote());

        return ResponseEntity.ok(patientDTO);
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<Patient> findAllPatients(){
        return this.serviceManager.findAll();
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
                    existing.getNote()
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

