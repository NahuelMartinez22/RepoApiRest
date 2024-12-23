package com.martinez.dentist.repositories;

import com.martinez.dentist.entities.Patient;
import org.springframework.data.repository.CrudRepository;

public interface PatientRepository extends CrudRepository<Patient, Long> {
}
