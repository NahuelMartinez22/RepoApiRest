package com.martinez.dentist.patients.services;

import com.martinez.dentist.patients.controllers.HealthInsuranceRequestDTO;

public interface HealthInsuranceService {
    String create(HealthInsuranceRequestDTO dto);
    void disable(Long id);
    void enable(Long id);
}

