package com.martinez.dentist.patients.services;

import com.martinez.dentist.patients.controllers.healthInsurance.HealthInsuranceRequestDTO;
import com.martinez.dentist.patients.controllers.healthInsurance.HealthInsuranceResponseDTO;

import java.util.List;
import java.util.Map;

public interface HealthInsuranceService {
    Map<String, Object> create(HealthInsuranceRequestDTO dto);
    void disable(Long id);
    void enable(Long id);
    HealthInsuranceResponseDTO update(Long id, HealthInsuranceRequestDTO dto);
    List<HealthInsuranceResponseDTO> findAll();
    List<HealthInsuranceResponseDTO> findAllActive();
    HealthInsuranceResponseDTO findById(Long id);


}

