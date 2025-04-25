package com.martinez.dentist.patients.controllers;

import com.martinez.dentist.patients.services.ClinicalHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clinical-history")
public class ClinicalHistoryController {

    @Autowired
    private ClinicalHistoryService clinicalHistoryService;

    @PostMapping
    @Transactional
    public ResponseEntity<String> createClinicalHistory(@RequestBody ClinicalHistoryRequestDTO dto) {
        return ResponseEntity.ok(clinicalHistoryService.createClinicalHistory(dto));
    }

    @GetMapping("/patient/{documentNumber}")
    public ResponseEntity<List<ClinicalHistoryResponseDTO>> getClinicalHistoryByPatient(@PathVariable String documentNumber) {
        return ResponseEntity.ok(clinicalHistoryService.getClinicalHistoryByPatient(documentNumber));
    }
}