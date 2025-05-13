package com.martinez.dentist.patients.controllers;

import com.martinez.dentist.patients.models.DentalProcedure;
import com.martinez.dentist.patients.repositories.DentalProcedureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dental-procedures")
public class DentalProcedureController {

    @Autowired
    private DentalProcedureRepository repository;

    // Obtener todas las prácticas activas
    @GetMapping
    public List<DentalProcedure> getAll() {
        return repository.findAllByIsActiveTrue();
    }

    // Cargar nueva práctica
    @PostMapping
    public DentalProcedure create(@RequestBody DentalProcedure procedure) {
        return repository.save(procedure);
    }

    // Desactivar práctica
    @PutMapping("/{id}/disable")
    public void disable(@PathVariable Long id) {
        DentalProcedure procedure = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Práctica no encontrada"));
        procedure.setActive(false);
        repository.save(procedure);
    }
}
