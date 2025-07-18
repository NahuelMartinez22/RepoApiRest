package com.martinez.dentist.professionals.controllers.professionalSpecialty;

import com.martinez.dentist.professionals.services.ProfessionalSpecialtyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professional-specialties")
public class ProfessionalSpecialtyController {

    private final ProfessionalSpecialtyService service;

    public ProfessionalSpecialtyController(ProfessionalSpecialtyService service) {
        this.service = service;
    }

    @PostMapping
    public ProfessionalSpecialtyResponseDTO assign(@RequestBody ProfessionalSpecialtyRequestDTO dto) {
        return service.assignSpecialty(dto);
    }

    @GetMapping("/by-professional/{professionalId}")
    public List<ProfessionalSpecialtyResponseDTO> getByProfessional(@PathVariable Long professionalId) {
        return service.getSpecialtiesByProfessional(professionalId);
    }

    @DeleteMapping
    public void deleteByProfessionalAndSpecialty(@RequestParam Long professionalId, @RequestParam Long specialtyId) {
        service.removeSpecialtyByProfessionalAndSpecialty(professionalId, specialtyId);
    }
}

