package com.martinez.dentist.patients.controllers;

import com.martinez.dentist.patients.services.InsurancePlanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insurance-plans")
public class InsurancePlanController {

    @Autowired
    private InsurancePlanService service;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid InsurancePlanRequestDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InsurancePlanResponseDTO> update(@PathVariable Long id, @RequestBody InsurancePlanRequestDTO dto) {
        InsurancePlanResponseDTO updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public List<InsurancePlanResponseDTO> getAllPlans() {
        return service.getAll();
    }

    @GetMapping("/by-health-insurance/{id}")
    public List<InsurancePlanResponseDTO> getPlansByHealthInsurance(@PathVariable Long id) {
        return service.getByHealthInsuranceId(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlan(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok("Plan eliminado con éxito.");
    }
}
