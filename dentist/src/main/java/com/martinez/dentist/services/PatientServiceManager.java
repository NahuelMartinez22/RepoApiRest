package com.martinez.dentist.services;

import com.martinez.dentist.entities.Patient;
import com.martinez.dentist.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceManager implements PatientService{

    @Autowired
    private PatientRepository repository;

    @Override
    public List<Patient> findAll() {
        return (List<Patient>) this.repository.findAll();
    }

    @Override
    public Patient findById(Long id) {
        return this.repository.findById(id).get();
    }

    @Override
    public Patient save(Patient patient) {
        return this.repository.save(patient);
    }
}
