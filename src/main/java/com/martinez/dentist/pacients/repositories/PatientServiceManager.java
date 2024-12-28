package com.martinez.dentist.pacients.repositories;

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

        Patient patient = this.repository.findById(id).orElse(null);

        if(patient == null) {
            throw new RuntimeException("Paciente no encontrado");
        }

        return patient;
    }

    public void save(Patient patient) {
        this.repository.save(patient);
    }

    public void disablePatient(Long id) {
        Patient patient = this.repository.findById(id).get();
        patient.disablePatient();
        repository.save(patient);
    }

    public void enablePatient(Long id) {
        Patient patient = this.repository.findById(id).get();
        patient.enablePatient();
        repository.save(patient);
    }
}
