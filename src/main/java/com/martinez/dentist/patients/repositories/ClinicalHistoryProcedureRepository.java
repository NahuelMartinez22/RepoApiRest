package com.martinez.dentist.patients.repositories;

import com.martinez.dentist.patients.models.ClinicalHistoryProcedure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClinicalHistoryProcedureRepository extends JpaRepository<ClinicalHistoryProcedure, Long> {
    List<ClinicalHistoryProcedure> findByClinicalHistoryId(Long historyId);
    void deleteByClinicalHistoryIdAndProcedureId(Long historyId, Long procedureId);
}