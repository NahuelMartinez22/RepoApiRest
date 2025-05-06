package com.martinez.dentist.patients.controllers;

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
    public ResponseEntity<String> createClinicalHistory(@RequestBody ClinicalHistoryRequestDTO dto) {
        return ResponseEntity.ok(clinicalHistoryService.createClinicalHistory(dto));
    }

    @GetMapping("/patient/{documentNumber}")
    public ResponseEntity<List<ClinicalHistoryResponseDTO>> getClinicalHistoryByPatient(@PathVariable String documentNumber) {
        return ResponseEntity.ok(clinicalHistoryService.getClinicalHistoryByPatient(documentNumber));
    }


    @PostMapping("/{id}/upload")
    public ResponseEntity<String> uploadFile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) throws IOException {

        ClinicalHistory history = clinicalHistoryService.getById(id);

        ClinicalFile clinicalFile = new ClinicalFile(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getBytes(),
                history
        );

        clinicalFileRepository.save(clinicalFile);
        return ResponseEntity.ok("Archivo subido correctamente.");
    }

    @GetMapping("/files/{fileId}/download")
    public void downloadFile(@PathVariable Long fileId, HttpServletResponse response) throws IOException {
        ClinicalFile file = clinicalFileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("Archivo no encontrado"));

        response.setContentType(file.getFileType());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getFileName() + "\"");
        response.getOutputStream().write(file.getData());
        response.getOutputStream().flush();
    }

    @DeleteMapping("/files/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable Long fileId) {
        clinicalFileRepository.deleteById(fileId);
        return ResponseEntity.ok("Archivo eliminado.");
    }
}