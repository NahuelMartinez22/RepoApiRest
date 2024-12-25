package com.martinez.dentist.repositories;

import com.martinez.dentist.models.Patient;
import org.springframework.data.repository.CrudRepository;

public interface PatientRepository extends CrudRepository<Patient, Long> {
}
