package com.martinez.dentist.patients.controllers;

import com.martinez.dentist.patients.models.DentalProcedure;
import com.martinez.dentist.patients.services.DentalProcedureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dental-procedures")
public class DentalProcedureController {

    @Autowired
    private DentalProcedureService service;

    @GetMapping
    public List<DentalProcedure> getAll() {
        return service.findAllActive();
    }

    @PostMapping
    public DentalProcedure create(@RequestBody DentalProcedure procedure) {
        return service.save(procedure);
    }

    @PutMapping("/{id}")
    public DentalProcedure update(@PathVariable Long id, @RequestBody DentalProcedure updatedProcedure) {
        return service.update(id, updatedProcedure);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Práctica eliminada con éxito.");
    }
}
