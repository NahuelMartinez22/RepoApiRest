package com.martinez.dentist.patients.controllers;

import com.martinez.dentist.patients.models.DentalProcedure;
import com.martinez.dentist.patients.repositories.DentalProcedureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dental-procedures")
public class DentalProcedureController {

    @Autowired
    private DentalProcedureRepository repository;

    @GetMapping
    public List<DentalProcedure> getAll() {
        return repository.findAllByIsActiveTrue();
    }

    @PostMapping
    public DentalProcedure create(@RequestBody DentalProcedure procedure) {
        return repository.save(procedure);
    }

    @PutMapping("/{id}")
    public DentalProcedure update(@PathVariable Long id, @RequestBody DentalProcedure updatedProcedure) {
        DentalProcedure existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Práctica no encontrada"));

        existing.setCode(updatedProcedure.getCode());
        existing.setName(updatedProcedure.getName());
        existing.setBaseValue(updatedProcedure.getBaseValue());

        return repository.save(existing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        DentalProcedure procedure = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Práctica no encontrada"));

        repository.delete(procedure);
        return ResponseEntity.ok("Práctica eliminada con éxito.");
    }
}
