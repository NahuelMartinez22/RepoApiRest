package com.martinez.dentist.patients.repositories;

import com.martinez.dentist.patients.models.Patient;
import com.martinez.dentist.patients.models.PatientState;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends CrudRepository<Patient, Long> {
    Optional<Patient> findByDocumentNumber(String documentNumber);
    List<Patient> findByPatientState(PatientState state);
}
