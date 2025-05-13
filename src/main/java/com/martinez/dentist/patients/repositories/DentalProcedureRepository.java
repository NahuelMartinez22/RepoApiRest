package com.martinez.dentist.patients.repositories;

import com.martinez.dentist.patients.models.DentalProcedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DentalProcedureRepository extends CrudRepository<DentalProcedure, Long> {
    List<DentalProcedure> findAllByIsActiveTrue();
}
