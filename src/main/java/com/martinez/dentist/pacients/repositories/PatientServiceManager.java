package com.martinez.dentist.pacients.repositories;

import com.martinez.dentist.pacients.controllers.PatientRequestDTO;
import com.martinez.dentist.pacients.models.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceManager {

    @Autowired
    private PatientRepository repository;


    public List<Patient> findAll() {
        return (List<Patient>) this.repository.findAll();
    }

    public Patient findById(Long id) {
        Optional<Patient> patient = this.repository.findById(id);
        if (patient.isEmpty()) {
            throw new RuntimeException("Paciente no encontrado");
        }
        return patient.get();
    }

    public void save(Patient patient) {
        this.repository.save(patient);
    }

    public Patient disablePatient(Long id) {
        Optional<Patient> patientOpt = this.repository.findById(id);
        if (patientOpt.isEmpty()) {
            throw new RuntimeException("Paciente no encontrado");
        }

        Patient patient = patientOpt.get();
        patient.disablePatient();
        repository.save(patient);
        return patient;
    }

    public Patient enablePatient(Long id) {
        Optional<Patient> patientOpt = this.repository.findById(id);
        if (patientOpt.isEmpty()) {
            throw new RuntimeException("Paciente no encontrado");
        }

        Patient patient = patientOpt.get();
        patient.enablePatient();
        repository.save(patient);
        return patient;
    }
}
