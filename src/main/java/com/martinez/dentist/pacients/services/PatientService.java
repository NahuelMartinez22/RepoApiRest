package com.martinez.dentist.pacients.services;

import com.martinez.dentist.pacients.controllers.PatientRequestDTO;
import com.martinez.dentist.pacients.controllers.PatientResponseDTO;
import com.martinez.dentist.pacients.models.PatientState;

import java.util.List;

public interface PatientService {

    String save(PatientRequestDTO dto);

    PatientResponseDTO findById(Long id);

    List<PatientResponseDTO> findAll();

    List<PatientResponseDTO> findByState(PatientState state);

    PatientResponseDTO update(Long id, PatientRequestDTO dto);

    void disable(Long id);

    void enable(Long id);
}
