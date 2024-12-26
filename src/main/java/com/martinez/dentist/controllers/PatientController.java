package com.martinez.dentist.controllers;

import com.martinez.dentist.models.Patient;
import com.martinez.dentist.models.PatientState;
import com.martinez.dentist.services.PatientServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<String> save(@RequestBody PatientRequestDTO patientRequestDTO){
        Patient patient = new Patient(patientRequestDTO.getFullName(),
                patientRequestDTO.getDocumentType(), patientRequestDTO.getDocumentNumber(),
                patientRequestDTO.getBirthDate(), patientRequestDTO.getRegistrationDate(),
                patientRequestDTO.getLastAppointmentDate());

        serviceManager.save(patient);

        return ResponseEntity.ok("El paciente se creo con exito.");
    }

    @GetMapping("/{id}")
    public PatientResponseDTO getByIdPatient(@PathVariable Long id){
        // BUSCA PACIENTE POR ID
        Patient patient = this.serviceManager.findById(id);

        // CONVIERTE DEL MODELO PACIENTE AL PASIENTE DTO
        PatientResponseDTO patientDTO = new PatientResponseDTO(patient.getId(),
                patient.getFullName(), patient.getDocumentType(), patient.getDocumentNumber(),
                patient.getBirthDate(), patient.getRegistrationDate(), patient.getLastVisitDate());

        // RETORNA EL PACIENTE DTO
        return patientDTO;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<Patient> findAllPatients(){
        return this.serviceManager.findAll();
    }

    @PatchMapping("/{id}/disable")
    public PatientResponseDTO disablePatient(@PathVariable Long id) {
        serviceManager.disablePatient(id);
        return null;
    }
    @PatchMapping("/{id}/enable")
    public PatientResponseDTO enablePatient(@PathVariable Long id) {
        serviceManager.enablePatient(id);
        return null;
    }
}
