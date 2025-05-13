package com.martinez.dentist.patients.repositories;

import com.martinez.dentist.patients.models.InsurancePlan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsurancePlanRepository extends CrudRepository<InsurancePlan, Long> {
    List<InsurancePlan> findByHealthInsuranceId(Long healthInsuranceId);
}