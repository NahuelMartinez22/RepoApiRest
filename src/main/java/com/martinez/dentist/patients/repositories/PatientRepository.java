package com.martinez.dentist.patients.repositories;

import com.martinez.dentist.patients.models.Patient;
import com.martinez.dentist.patients.models.PatientState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByDocumentNumber(String documentNumber);

    List<Patient> findByPatientState(PatientState state);

    @Query("SELECT p FROM Patient p WHERE " +
            "p.patientState = com.martinez.dentist.patients.models.PatientState.ACTIVE AND " +
            "(LOWER(p.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.documentNumber) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Patient> findBySearch(@Param("search") String search, Pageable pageable);

    @Query("SELECT p FROM Patient p WHERE p.patientState = com.martinez.dentist.patients.models.PatientState.ACTIVE")
    Page<Patient> findAllActive(Pageable pageable);

    @Query("SELECT FUNCTION('MONTH', p.registrationDate), COUNT(p) " +
            "FROM Patient p " +
            "WHERE p.registrationDate >= :desde " +
            "AND p.patientState = com.martinez.dentist.patients.models.PatientState.ACTIVE " +
            "GROUP BY FUNCTION('MONTH', p.registrationDate)")
    List<Object[]> countNewPatientsPerMonth(@Param("desde") LocalDate desde);

    @EntityGraph(attributePaths = {"healthInsurance", "insurancePlan"})
    List<Patient> findByIsGuestTrue();
}