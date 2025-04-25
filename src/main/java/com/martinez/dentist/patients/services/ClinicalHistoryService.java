package com.martinez.dentist.patients.services;


import com.martinez.dentist.patients.controllers.ClinicalHistoryRequestDTO;
import com.martinez.dentist.patients.controllers.ClinicalHistoryResponseDTO;

import java.util.List;

public interface ClinicalHistoryService {
    String createClinicalHistory(ClinicalHistoryRequestDTO dto);
    List<ClinicalHistoryResponseDTO> getClinicalHistoryByPatient(String documentNumber);
}