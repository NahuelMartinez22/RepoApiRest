package com.martinez.dentist.patients.controllers;

import com.martinez.dentist.patients.models.HealthInsurance;
import com.martinez.dentist.patients.models.InsurancePlan;
import com.martinez.dentist.patients.repositories.HealthInsuranceRepository;
import com.martinez.dentist.patients.repositories.InsurancePlanRepository;
import com.martinez.dentist.patients.services.HealthInsuranceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/health-insurances")
public class HealthInsuranceController {

    @Autowired
    private HealthInsuranceRepository healthInsuranceRepository;

    @Autowired
    private InsurancePlanRepository insurancePlanRepository;

    @Autowired
    private HealthInsuranceService service;

    @GetMapping
    public List<HealthInsurance> getAllHealthInsurances() {
        return (List<HealthInsurance>) healthInsuranceRepository.findAll();
    }

    @GetMapping("/{id}/plans")
    public List<InsurancePlan> getPlansByHealthInsurance(@PathVariable Long id) {
        return insurancePlanRepository.findByHealthInsuranceId(id);
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid HealthInsuranceRequestDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}/disable")
    public ResponseEntity<String> disable(@PathVariable Long id) {
        service.disable(id);
        return ResponseEntity.ok("Obra social desactivada con éxito.");
    }

    @PutMapping("/{id}/enable")
    public ResponseEntity<String> enable(@PathVariable Long id) {
        service.enable(id);
        return ResponseEntity.ok("Obra social activada con éxito.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<HealthInsurance> getById(@PathVariable Long id) {
        HealthInsurance hi = healthInsuranceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Obra social no encontrada"));
        return ResponseEntity.ok(hi);
    }

    @GetMapping("/active")
    public List<HealthInsurance> getActiveHealthInsurances() {
        return healthInsuranceRepository.findAllByIsActiveTrue();
    }

}
