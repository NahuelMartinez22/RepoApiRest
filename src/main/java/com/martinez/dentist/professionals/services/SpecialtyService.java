package com.martinez.dentist.professionals.services;

import com.martinez.dentist.professionals.controllers.specialty.SpecialtyRequestDTO;
import com.martinez.dentist.professionals.controllers.specialty.SpecialtyResponseDTO;

import java.util.List;

public interface SpecialtyService {
    SpecialtyResponseDTO addSpecialty(SpecialtyRequestDTO dto);
    SpecialtyResponseDTO updateSpecialty(Long id, SpecialtyRequestDTO dto);
    List<SpecialtyResponseDTO> getAllSpecialties();
    void deleteSpecialty(Long id);
}
