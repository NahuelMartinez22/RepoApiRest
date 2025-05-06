package com.martinez.dentist.patients.services;


import com.martinez.dentist.patients.controllers.ClinicalHistoryRequestDTO;
import com.martinez.dentist.patients.controllers.ClinicalHistoryResponseDTO;
import com.martinez.dentist.patients.models.ClinicalHistory;

import java.util.List;

public interface ClinicalHistoryService {
    String createClinicalHistory(ClinicalHistoryRequestDTO dto);
    List<ClinicalHistoryResponseDTO> getClinicalHistoryByPatient(String documentNumber);
    ClinicalHistory getById(Long id);
}