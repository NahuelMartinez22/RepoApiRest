package com.martinez.dentist.professionals.controllers.specialty;

import com.martinez.dentist.professionals.services.SpecialtyService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/specialties")
public class SpecialtyController {

    private final SpecialtyService service;

    public SpecialtyController(SpecialtyService service) {
        this.service = service;
    }

    @PostMapping
    public SpecialtyResponseDTO create(@RequestBody SpecialtyRequestDTO dto) {
        return service.addSpecialty(dto);
    }

    @PutMapping("/{id}")
    public SpecialtyResponseDTO update(@PathVariable Long id, @RequestBody SpecialtyRequestDTO dto) {
        return service.updateSpecialty(id, dto);
    }

    @GetMapping
    public List<SpecialtyResponseDTO> listAll() {
        return service.getAllSpecialties();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteSpecialty(id);
    }
}
