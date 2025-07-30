package com.martinez.dentist.patients.controllers.clinicalHistory;

import com.martinez.dentist.patients.models.ClinicalFile;
import com.martinez.dentist.patients.models.ClinicalHistory;
import com.martinez.dentist.patients.repositories.ClinicalFileRepository;
import com.martinez.dentist.patients.services.ClinicalHistoryService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/clinical-history")
public class ClinicalHistoryController {

    @Autowired
    private ClinicalHistoryService clinicalHistoryService;

    @Autowired
    private ClinicalFileRepository clinicalFileRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<ClinicalHistoryResponseDTO> createClinicalHistory(@RequestBody ClinicalHistoryRequestDTO dto) {
        ClinicalHistoryResponseDTO response = clinicalHistoryService.createClinicalHistory(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/patient/{documentNumber}")
    public ResponseEntity<List<ClinicalHistoryResponseDTO>> getClinicalHistoryByPatient(@PathVariable String documentNumber) {
        return ResponseEntity.ok(clinicalHistoryService.getClinicalHistoryByPatient(documentNumber));
    }

    @GetMapping
    public ResponseEntity<List<ClinicalHistoryResponseDTO>> getAllClinicalHistories() {
        return ResponseEntity.ok(clinicalHistoryService.getAll());
    }


    @PostMapping("/{id}/upload")
    public ResponseEntity<UploadResponseDTO> uploadFile(
            @PathVariable Long id,
            @RequestParam("file") List<MultipartFile> files) throws IOException {

        List<ClinicalFile> uploaded = clinicalHistoryService.uploadFiles(id, files);

        List<UploadedFileResponseDTO> fileDTOs = uploaded.stream()
                .map(f -> new UploadedFileResponseDTO(f.getId(), f.getFileName()))
                .toList();

        return ResponseEntity.ok(new UploadResponseDTO("Archivos subidos correctamente.", fileDTOs));
    }

    @GetMapping("/files/{fileId}/download")
    public void downloadFile(@PathVariable Long fileId, HttpServletResponse response) throws IOException {
        clinicalHistoryService.downloadFile(fileId, response);
    }

    @DeleteMapping("/files/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable Long fileId) {
        return clinicalHistoryService.deleteFile(fileId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClinicalHistory(@PathVariable Long id) {
        clinicalHistoryService.deleteClinicalHistory(id);
        return ResponseEntity.ok("Historia clínica eliminada correctamente.");
    }

    @PutMapping("/{id}/description")
    public ResponseEntity<String> updateClinicalHistoryDescription(
            @PathVariable Long id,
            @RequestBody ClinicalHistoryRequestDTO dto) {
        return ResponseEntity.ok(clinicalHistoryService.updateClinicalHistoryDescription(id, dto));
    }

    @PostMapping("/{id}/procedures")
    public ResponseEntity<String> addMultipleProcedures(
            @PathVariable Long id,
            @RequestBody ProcedureIdsRequestDTO dto) {
        return ResponseEntity.ok(clinicalHistoryService.addProcedures(id, dto.getProcedureIds()));
    }

    @DeleteMapping("/{id}/procedures")
    public ResponseEntity<String> removeMultipleProcedures(
            @PathVariable Long id,
            @RequestBody ProcedureIdsRequestDTO dto) {
        return ResponseEntity.ok(clinicalHistoryService.removeProcedures(id, dto.getProcedureIds()));
    }
}