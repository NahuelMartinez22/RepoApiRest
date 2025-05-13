package com.martinez.dentist.patients.services;

import com.martinez.dentist.patients.controllers.HealthInsuranceRequestDTO;
import com.martinez.dentist.patients.models.HealthInsurance;
import com.martinez.dentist.patients.repositories.HealthInsuranceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HealthInsuranceServiceImpl implements HealthInsuranceService {

    @Autowired
    private HealthInsuranceRepository repository;

    @Override
    public String create(HealthInsuranceRequestDTO dto) {
        if (repository.existsByName(dto.getName())) {
            throw new RuntimeException("Ya existe una obra social con ese nombre");
        }

        HealthInsurance hi = new HealthInsurance(
                dto.getName(),
                dto.getContactEmail(),
                dto.getPhone(),
                dto.getNote()
        );

        repository.save(hi);
        return "Obra social creada con éxito.";
    }

    @Override
    public void disable(Long id) {
        HealthInsurance hi = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Obra social no encontrada"));

        hi.setIsActive(false);
        repository.save(hi);
    }
    @Override
    public void enable(Long id) {
        HealthInsurance hi = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Obra social no encontrada"));

        hi.setIsActive(true);
        repository.save(hi);
    }
}

