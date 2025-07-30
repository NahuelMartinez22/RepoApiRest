package com.martinez.dentist.patients.services;

import com.martinez.dentist.patients.models.DentalProcedure;

import java.util.List;

public interface DentalProcedureService {
    List<DentalProcedure> findAllActive();
    DentalProcedure save(DentalProcedure procedure);
    DentalProcedure update(Long id, DentalProcedure updatedProcedure);
    void delete(Long id);
}
