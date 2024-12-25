package com.martinez.dentist.services;

import com.martinez.dentist.models.Patient;
import com.martinez.dentist.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceManager {

    @Autowired
    private PatientRepository repository;


    public List<Patient> findAll() {
        return (List<Patient>) this.repository.findAll();
    }

    public Patient findById(Long id) {
        return this.repository.findById(id).get();
    }

    public void save(Patient patient) {
        this.repository.save(patient);
    }

    public void deactivate(Long id) {
        // buscar el paciente
        // cambiar el estado de activate a deactive
        //

        repository.
    }
}
