package com.martinez.dentist.patients.controllers.healthInsurance;

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
    public ResponseEntity<HealthInsuranceResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<HealthInsuranceResponseDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<HealthInsuranceResponseDTO>> getAllActive() {
        List<HealthInsuranceResponseDTO> list = service.findAllActive();
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HealthInsuranceResponseDTO> update(@PathVariable Long id, @RequestBody HealthInsuranceRequestDTO dto) {
        HealthInsuranceResponseDTO response = service.update(id, dto);
        return ResponseEntity.ok(response);
    }

}
