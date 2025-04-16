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
    public ResponseEntity<String> save(@RequestBody PatientRequestDTO patientRequestDTO) {
        Patient patient = new Patient(patientRequestDTO.getFullName(),
                patientRequestDTO.getDocumentType(),
                patientRequestDTO.getDocumentNumber(),
                patientRequestDTO.getHealthInsurance(),
                patientRequestDTO.getInsurancePlan(),
                patientRequestDTO.getPhone(),
                patientRequestDTO.getRegistrationDate(),
                patientRequestDTO.getLastVisitDate()
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
                patient.getRegistrationDate(),patient.getLastVisitDate());
        
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
}

