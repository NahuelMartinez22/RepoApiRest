package com.martinez.dentist.professionals.controllers.professional;

import com.martinez.dentist.professionals.controllers.schedule.ScheduleResponseDTO;
import com.martinez.dentist.professionals.models.Professional;
import com.martinez.dentist.professionals.services.ProfessionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professionals")
@CrossOrigin
public class ProfessionalController {

    @Autowired
    private ProfessionalService professionalService;

    @GetMapping
    public ResponseEntity<List<ProfessionalResponseDTO>> getAllProfessionals() {
        List<Professional> professionals = professionalService.findAll();

        List<ProfessionalResponseDTO> response = professionals.stream().map(prof -> new ProfessionalResponseDTO(
                prof.getId(),
                prof.getFullName(),
                prof.getDocumentType(),
                prof.getDocumentNumber(),
                prof.getPhone(),
                prof.getSchedules().stream().map(schedule -> new ScheduleResponseDTO(
                        schedule.getDayOfWeek(),
                        schedule.getStartTime(),
                        schedule.getEndTime()
                )).toList(),
                prof.getProfessionalState().getDisplayName()
        )).toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    public ResponseEntity<List<ProfessionalResponseDTO>> getAllActiveProfessionals() {
        List<Professional> professionals = professionalService.findAllActive();

        List<ProfessionalResponseDTO> response = professionals.stream().map(prof -> new ProfessionalResponseDTO(
                prof.getId(),
                prof.getFullName(),
                prof.getDocumentType(),
                prof.getDocumentNumber(),
                prof.getPhone(),
                prof.getSchedules().stream().map(schedule -> new ScheduleResponseDTO(
                        schedule.getDayOfWeek(),
                        schedule.getStartTime(),
                        schedule.getEndTime()
                )).toList(),
                prof.getProfessionalState().getDisplayName()
        )).toList();

        return ResponseEntity.ok(response);
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
}
