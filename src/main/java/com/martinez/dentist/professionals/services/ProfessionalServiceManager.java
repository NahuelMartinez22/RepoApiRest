package com.martinez.dentist.professionals.services;

import com.martinez.dentist.professionals.models.Professional;
import com.martinez.dentist.professionals.models.ProfessionalState;
import com.martinez.dentist.professionals.repositories.ProfessionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessionalServiceManager {

    @Autowired
    private ProfessionalRepository repository;

    public List<Professional> findAll() {
        return (List<Professional>) repository.findAll();
    }

    public Optional<Professional> findByDocumentNumber(String documentNumber) {
        return repository.findByDocumentNumber(documentNumber);
    }

    public boolean existsByDocumentNumber(String documentNumber) {
        return repository.findByDocumentNumber(documentNumber).isPresent();
    }

    public void save(Professional professional) {
        repository.save(professional);
    }

    public List<Professional> findByState(ProfessionalState state) {
        return repository.findAllByProfessionalState(state);
    }

}
