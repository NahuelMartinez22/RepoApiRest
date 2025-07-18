package com.martinez.dentist.professionals.services;

import com.martinez.dentist.professionals.controllers.professional.ProfessionalRequestDTO;
import com.martinez.dentist.professionals.controllers.professional.ProfessionalResponseDTO;

import com.martinez.dentist.professionals.models.Professional;

import java.util.List;

public interface ProfessionalService {

    Professional create(ProfessionalRequestDTO dto);

    Professional updateById(Long id, ProfessionalRequestDTO dto);

    Professional disableByDocumentNumber(String documentNumber);

    Professional enableByDocumentNumber(String documentNumber);

    List<ProfessionalResponseDTO> findAll();

    List<ProfessionalResponseDTO> findAllActive();
}
