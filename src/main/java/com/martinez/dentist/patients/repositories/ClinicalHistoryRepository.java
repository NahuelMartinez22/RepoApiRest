package com.martinez.dentist.patients.repositories;

import com.martinez.dentist.patients.models.ClinicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClinicalHistoryRepository extends JpaRepository<ClinicalHistory, Long> {
    List<ClinicalHistory> findByPatientId(Long patientId);
}
