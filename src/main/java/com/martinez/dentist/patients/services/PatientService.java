package com.martinez.dentist.patients.services;

import com.martinez.dentist.patients.controllers.PatientRequestDTO;
import com.martinez.dentist.patients.controllers.PatientResponseDTO;
import com.martinez.dentist.patients.models.PatientState;

import java.util.List;

public interface PatientService {

    PatientResponseDTO save(PatientRequestDTO dto);

    PatientResponseDTO findById(Long id);

    List<PatientResponseDTO> findAll();

    List<PatientResponseDTO> findByState(PatientState state);

    PatientResponseDTO update(Long id, PatientRequestDTO dto);

    void disable(Long id);

    void enable(Long id);
}
