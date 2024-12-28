package com.martinez.dentist.pacients.repositories;

import com.martinez.dentist.pacients.models.Patient;
import org.springframework.data.repository.CrudRepository;

public interface PatientRepository extends CrudRepository<Patient, Long> {
}
