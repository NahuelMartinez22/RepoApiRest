package com.martinez.dentist.professionals.services;

import com.martinez.dentist.professionals.controllers.ProfessionalRequestDTO;
import com.martinez.dentist.professionals.models.Professional;

import java.util.List;

public interface ProfessionalService {
    Professional create(ProfessionalRequestDTO dto);
    Professional updateByDocumentNumber(String documentNumber, ProfessionalRequestDTO dto);
    List<Professional> findAll();
    List<Professional> findAllActive();
    Professional disableByDocumentNumber(String documentNumber);
    Professional enableByDocumentNumber(String documentNumber);
}
