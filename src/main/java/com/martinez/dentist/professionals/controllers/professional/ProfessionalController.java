package com.martinez.dentist.professionals.controllers.professional;

import com.martinez.dentist.professionals.models.Professional;
import com.martinez.dentist.professionals.services.ProfessionalService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professionals")
public class ProfessionalController {

    private final ProfessionalService professionalService;

    public ProfessionalController(ProfessionalService professionalService) {
        this.professionalService = professionalService;
    }

    @GetMapping
    public List<ProfessionalResponseDTO> getAll() {
        return professionalService.findAll();
    }

    @GetMapping("/active")
    public List<ProfessionalResponseDTO> getAllActive() {
        return professionalService.findAllActive();
    }

    @PostMapping
    @Transactional
    public ResponseEntity<String> saveProfessional(@RequestBody ProfessionalRequestDTO dto) {
        try {
            professionalService.create(dto);
            return ResponseEntity.ok("Profesional creado con éxito.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> updateProfessionalById(@PathVariable Long id,
                                                    @RequestBody ProfessionalRequestDTO dto) {
        professionalService.updateById(id, dto);
        return ResponseEntity.ok("Profesional actualizado.");
    }

    @PatchMapping("/document/{documentNumber}/disable")
    public ResponseEntity<String> disableProfessionalByDocument(@PathVariable String documentNumber) {
        professionalService.disableByDocumentNumber(documentNumber);
        return ResponseEntity.ok("Profesional deshabilitado con éxito.");
    }

    @PatchMapping("/document/{documentNumber}/enable")
    public ResponseEntity<String> enableProfessionalByDocument(@PathVariable String documentNumber) {
        professionalService.enableByDocumentNumber(documentNumber);
        return ResponseEntity.ok("Profesional habilitado con éxito.");
    }

    @PatchMapping("/{id}/available")
    public ResponseEntity<String> setAvailable(@PathVariable Long id, @RequestParam boolean available) {
        professionalService.setAvailable(id, available);
        String estado = available ? "disponible" : "no disponible";
        return ResponseEntity.ok("Disponibilidad actualizada: el profesional ahora está " + estado + ".");
    }

    @GetMapping("/available")
    public ResponseEntity<List<Professional>> getAvailableProfessionals() {
        return ResponseEntity.ok(professionalService.getAvailableProfessionals());
    }
}
