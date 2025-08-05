package com.martinez.dentist.patients.services;


import com.martinez.dentist.patients.controllers.clinicalHistory.ClinicalHistoryRequestDTO;
import com.martinez.dentist.patients.controllers.clinicalHistory.ClinicalHistoryResponseDTO;
import com.martinez.dentist.patients.models.ClinicalFile;
import com.martinez.dentist.patients.models.ClinicalHistory;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ClinicalHistoryService {
    ClinicalHistoryResponseDTO createClinicalHistory(ClinicalHistoryRequestDTO dto);
    List<ClinicalHistoryResponseDTO> getClinicalHistoryByPatient(String documentNumber);
    List<ClinicalHistoryResponseDTO> getAll();
    ClinicalHistory getById(Long id);
    void deleteClinicalHistory(Long id);
    String updateClinicalHistoryDescription(Long id, ClinicalHistoryRequestDTO dto);
    String patchProcedures(Long historyId, List<Long> procedureIds);
    String removeProcedures(Long historyId, List<Long> procedureIds);

    List<ClinicalFile> uploadFiles(Long historyId, List<MultipartFile> files) throws IOException;
    void downloadFile(Long fileId, HttpServletResponse response) throws IOException;
    ResponseEntity<String> deleteFile(Long fileId);

    String updateProcedures(Long historyId, List<Long> procedureIds);


}