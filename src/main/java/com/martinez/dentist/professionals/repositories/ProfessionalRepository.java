package com.martinez.dentist.professionals.repositories;

import com.martinez.dentist.professionals.models.Professional;
import com.martinez.dentist.professionals.models.ProfessionalState;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProfessionalRepository extends CrudRepository<Professional, Long> {

    Optional<Professional> findByDocumentNumber(String documentNumber);
    List<Professional> findAllByProfessionalState(ProfessionalState professionalState);
}
