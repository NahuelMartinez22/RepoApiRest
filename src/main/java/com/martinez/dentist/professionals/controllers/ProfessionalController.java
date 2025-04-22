package com.martinez.dentist.professionals.controllers;

import com.martinez.dentist.professionals.models.Professional;
import com.martinez.dentist.professionals.models.ProfessionalState;
import com.martinez.dentist.professionals.services.ProfessionalServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professionals")
public class ProfessionalController {

    @Autowired
    private ProfessionalServiceManager professionalService;


    @GetMapping
    public ResponseEntity<List<ProfessionalResponseDTO>> getAllProfessionals() {
        List<Professional> professionals = professionalService.findAll();

        List<ProfessionalResponseDTO> response = professionals.stream().map(prof -> new ProfessionalResponseDTO(
                prof.getId(),
                prof.getFullName(),
                prof.getDocumentType(),
                prof.getDocumentNumber(),
                prof.getPhone(),
                prof.getStartTime(),
                prof.getEndTime(),
                prof.getProfessionalState().getDisplayName()
        )).toList();

        return ResponseEntity.ok(response);
    }


    @PostMapping
    @Transactional
    public ResponseEntity<String> saveProfessional(@RequestBody ProfessionalRequestDTO dto) {
        if (professionalService.existsByDocumentNumber(dto.getDocumentNumber())) {
            return ResponseEntity.badRequest().body("Ya existe un profesional con el mismo numero de documento.");
        }

        Professional professional = new Professional(
                dto.getFullName(),
                dto.getDocumentType(),
                dto.getDocumentNumber(),
                dto.getPhone(),
                dto.getStartTime(),
                dto.getEndTime()
        );

        professionalService.save(professional);
        return ResponseEntity.ok("Profesional creado con éxito.");
    }


    @PutMapping("/document/{documentNumber}")
    @Transactional
    public ResponseEntity<?> updateProfessionalByDocument(@PathVariable String documentNumber,
                                                          @RequestBody ProfessionalRequestDTO dto) {
        try {
            Professional prof = professionalService.findByDocumentNumber(documentNumber)
                    .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));

            prof.updateData(dto);
            professionalService.save(prof);
            return ResponseEntity.ok("Profesional actualizado.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró el profesional con documento: " + documentNumber);
        }
    }


    @PatchMapping("/document/{documentNumber}/disable")
    public ResponseEntity<String> disableProfessionalByDocument(@PathVariable String documentNumber) {
        try {
            Professional professional = professionalService.findByDocumentNumber(documentNumber)
                    .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));

            professional.disable();
            professionalService.save(professional);

            return ResponseEntity.ok("Profesional deshabilitado con éxito.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró el profesional con documento: " + documentNumber);
        }
    }

    @PatchMapping("/document/{documentNumber}/enable")
    public ResponseEntity<String> enableProfessionalByDocument(@PathVariable String documentNumber) {
        try {
            Professional professional = professionalService.findByDocumentNumber(documentNumber)
                    .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));

            professional.enable();
            professionalService.save(professional);

            return ResponseEntity.ok("Profesional habilitado con éxito.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró el profesional con documento: " + documentNumber);
        }
    }
}
