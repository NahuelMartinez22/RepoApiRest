package com.martinez.dentist.professionals.services;

import com.martinez.dentist.professionals.controllers.professionalSpecialty.ProfessionalSpecialtyRequestDTO;
import com.martinez.dentist.professionals.controllers.professionalSpecialty.ProfessionalSpecialtyResponseDTO;

import java.util.List;

public interface ProfessionalSpecialtyService {
    ProfessionalSpecialtyResponseDTO assignSpecialty(ProfessionalSpecialtyRequestDTO dto);
    List<ProfessionalSpecialtyResponseDTO> getSpecialtiesByProfessional(Long professionalId);
    void removeSpecialtyByProfessionalAndSpecialty(Long professionalId, Long specialtyId);
}
