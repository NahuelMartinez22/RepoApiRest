package com.martinez.dentist.patients.services;


import com.martinez.dentist.patients.controllers.clinicalHistory.ClinicalHistoryRequestDTO;
import com.martinez.dentist.patients.controllers.clinicalHistory.ClinicalHistoryResponseDTO;
import com.martinez.dentist.patients.models.ClinicalFile;
import com.martinez.dentist.patients.models.ClinicalHistory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ClinicalHistoryService {
    String createClinicalHistory(ClinicalHistoryRequestDTO dto);
    List<ClinicalHistoryResponseDTO> getClinicalHistoryByPatient(String documentNumber);
    List<ClinicalHistoryResponseDTO> getAll();
    ClinicalHistory getById(Long id);
    void deleteClinicalHistory(Long id);
    String updateClinicalHistoryDescription(Long id, ClinicalHistoryRequestDTO dto);
    String addProcedures(Long historyId, List<Long> procedureIds);
    String removeProcedures(Long historyId, List<Long> procedureIds);

    List<ClinicalFile> uploadFiles(Long historyId, List<MultipartFile> files) throws IOException;

}