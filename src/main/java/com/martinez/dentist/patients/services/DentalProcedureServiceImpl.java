package com.martinez.dentist.patients.services;

import com.martinez.dentist.patients.models.DentalProcedure;
import com.martinez.dentist.patients.repositories.DentalProcedureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class DentalProcedureServiceImpl implements DentalProcedureService {

    @Autowired
    private DentalProcedureRepository repository;

    @Override
    public List<DentalProcedure> findAllActive() {
        return repository.findAllByIsActiveTrue();
    }

    @Override
    public DentalProcedure save(DentalProcedure procedure) {

        if (!StringUtils.hasText(procedure.getName())) {
            throw new IllegalArgumentException("El nombre de la práctica no puede estar vacío.");
        }
        if (!StringUtils.hasText(procedure.getCode())) {
            throw new IllegalArgumentException("El código de la práctica no puede estar vacío.");
        }
        if (repository.existsByCode(procedure.getCode())) {
            throw new IllegalArgumentException("Ya existe una práctica con ese código.");
        }

        if (procedure.getBaseValue() == null || procedure.getBaseValue().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El valor base debe ser un número positivo.");
        }

        return repository.save(procedure);
    }

    @Override
    public DentalProcedure update(Long id, DentalProcedure updatedProcedure) {
        DentalProcedure existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Práctica no encontrada"));


        if (!StringUtils.hasText(updatedProcedure.getName())) {
            throw new IllegalArgumentException("El nombre de la práctica no puede estar vacío.");
        }

        if (!StringUtils.hasText(updatedProcedure.getCode())) {
            throw new IllegalArgumentException("El código de la práctica no puede estar vacío.");
        }

        if (!existing.getCode().equals(updatedProcedure.getCode())) {
            if (repository.existsByCode(updatedProcedure.getCode())) {
                throw new IllegalArgumentException("Ya existe una práctica con ese código.");
            }
        }
        if (updatedProcedure.getBaseValue() == null || updatedProcedure.getBaseValue().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El valor base debe ser un número positivo.");
        }

        existing.setCode(updatedProcedure.getCode());
        existing.setName(updatedProcedure.getName());
        existing.setBaseValue(updatedProcedure.getBaseValue());
        return repository.save(existing);
    }

    @Override
    public void delete(Long id) {
        DentalProcedure procedure = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Práctica no encontrada"));
        repository.delete(procedure);
    }
}
