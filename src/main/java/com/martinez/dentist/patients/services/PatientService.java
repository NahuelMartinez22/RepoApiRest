package com.martinez.dentist.patients.services;

import com.martinez.dentist.patients.controllers.PatientRequestDTO;
import com.martinez.dentist.patients.controllers.PatientResponseDTO;
import com.martinez.dentist.patients.models.PatientState;

import java.util.List;

public interface PatientService {

    Long save(PatientRequestDTO dto);

    Long update(Long id, PatientRequestDTO dto);

    PatientResponseDTO findById(Long id);

    List<PatientResponseDTO> findAll();

    List<PatientResponseDTO> findByState(PatientState state);

    void disable(Long id);

    void enable(Long id);

    List<PatientResponseDTO> findAllGuests();
}
