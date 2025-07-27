package com.martinez.dentist.patients.services;

import com.martinez.dentist.patients.controllers.healthInsurance.HealthInsuranceRequestDTO;
import com.martinez.dentist.patients.controllers.healthInsurance.HealthInsuranceResponseDTO;

import java.util.List;

public interface HealthInsuranceService {
    String create(HealthInsuranceRequestDTO dto);
    void disable(Long id);
    void enable(Long id);
    HealthInsuranceResponseDTO update(Long id, HealthInsuranceRequestDTO dto);
    List<HealthInsuranceResponseDTO> findAll();
    List<HealthInsuranceResponseDTO> findAllActive();
    HealthInsuranceResponseDTO findById(Long id);


}

