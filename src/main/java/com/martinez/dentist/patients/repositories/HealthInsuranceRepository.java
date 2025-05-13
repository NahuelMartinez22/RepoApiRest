package com.martinez.dentist.patients.repositories;

import com.martinez.dentist.patients.models.HealthInsurance;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthInsuranceRepository extends CrudRepository<HealthInsurance, Long> {
    boolean existsByName(String name);
    List<HealthInsurance> findAllByIsActiveTrue();
}
