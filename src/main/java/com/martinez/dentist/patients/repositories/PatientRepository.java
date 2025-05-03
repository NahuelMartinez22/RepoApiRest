package com.martinez.dentist.patients.repositories;

import com.martinez.dentist.patients.models.Patient;
import com.martinez.dentist.patients.models.PatientState;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PatientRepository extends CrudRepository<Patient, Long> {

    Optional<Patient> findByDocumentNumber(String documentNumber);

    List<Patient> findByPatientState(PatientState state);


    @Query("SELECT FUNCTION('MONTH', p.registrationDate), COUNT(p) " +
            "FROM Patient p " +
            "WHERE p.registrationDate >= :desde " +
            "AND p.patientState = com.martinez.dentist.patients.models.PatientState.ACTIVE " +
            "GROUP BY FUNCTION('MONTH', p.registrationDate)")
    List<Object[]> countNewPatientsPerMonth(@Param("desde") LocalDate desde);
}
