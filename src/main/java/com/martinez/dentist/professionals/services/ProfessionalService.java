package com.martinez.dentist.professionals.services;

import com.martinez.dentist.professionals.controllers.professional.ProfessionalRequestDTO;
import com.martinez.dentist.professionals.controllers.professional.ProfessionalResponseDTO;

import com.martinez.dentist.professionals.models.Professional;

import java.time.LocalDate;
import java.util.List;

public interface ProfessionalService {

    Long create(ProfessionalRequestDTO dto);

    Long updateById(Long id, ProfessionalRequestDTO dto);

    Professional disableByDocumentNumber(String documentNumber);

    Professional enableByDocumentNumber(String documentNumber);

    List<ProfessionalResponseDTO> findAll();

    List<ProfessionalResponseDTO> findAllActive();

    void setAvailable(Long professionalId, boolean available);
    List<ProfessionalResponseDTO> getAvailableProfessionals();

    void assignLicenseDates(Long professionalId, LocalDate startDate, LocalDate endDate);

}
