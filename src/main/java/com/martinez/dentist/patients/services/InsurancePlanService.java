package com.martinez.dentist.patients.services;

import com.martinez.dentist.patients.controllers.InsurancePlan.InsurancePlanRequestDTO;
import com.martinez.dentist.patients.controllers.InsurancePlan.InsurancePlanResponseDTO;

import java.util.List;
import java.util.Map;

public interface InsurancePlanService {
    Map<String, Object> create(InsurancePlanRequestDTO dto);
    List<InsurancePlanResponseDTO> getAll();
    List<InsurancePlanResponseDTO> getByHealthInsuranceId(Long id);
    void deleteById(Long id);
    InsurancePlanResponseDTO update(Long id, InsurancePlanRequestDTO dto);

}