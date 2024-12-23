package com.martinez.dentist.services;

import com.martinez.dentist.entities.Patient;

import java.util.List;

public interface PatientService {

    List<Patient> findAll();

    Patient findById(Long id);

    Patient save(Patient patient);
}
