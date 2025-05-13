package com.martinez.dentist.patients.services;

import com.martinez.dentist.patients.controllers.InsurancePlanRequestDTO;
import com.martinez.dentist.patients.controllers.InsurancePlanResponseDTO;

import java.util.List;

public interface InsurancePlanService {
    String create(InsurancePlanRequestDTO dto);
    List<InsurancePlanResponseDTO> getAll();
    List<InsurancePlanResponseDTO> getByHealthInsuranceId(Long id);
    void deleteById(Long id);
}