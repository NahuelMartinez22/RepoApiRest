package com.martinez.dentist.patients.services;

import com.martinez.dentist.patients.controllers.healthInsurance.HealthInsuranceRequestDTO;
import com.martinez.dentist.patients.controllers.healthInsurance.HealthInsuranceResponseDTO;
import com.martinez.dentist.patients.controllers.InsurancePlanDTO;
import com.martinez.dentist.patients.models.HealthInsurance;
import com.martinez.dentist.patients.repositories.HealthInsuranceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

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

    @Override
    public HealthInsuranceResponseDTO update(Long id, HealthInsuranceRequestDTO dto) {
        HealthInsurance hi = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Obra social no encontrada"));

        boolean hasChanges = false;

        if (!hi.getName().equals(dto.getName())) {
            if (repository.existsByName(dto.getName())) {
                throw new RuntimeException("Ya existe una obra social con ese nombre");
            }
            hi.setName(dto.getName());
            hasChanges = true;
        }

        if (!hi.getContactEmail().equals(dto.getContactEmail())) {
            hi.setContactEmail(dto.getContactEmail());
            hasChanges = true;
        }

        if (!hi.getPhone().equals(dto.getPhone())) {
            hi.setPhone(dto.getPhone());
            hasChanges = true;
        }

        if (!hi.getNote().equals(dto.getNote())) {
            hi.setNote(dto.getNote());
            hasChanges = true;
        }

        if (!hasChanges) {
            throw new RuntimeException("No se detectaron cambios para actualizar");
        }

        repository.save(hi);

        return new HealthInsuranceResponseDTO(
                hi.getId(),
                hi.getName(),
                hi.getContactEmail(),
                hi.getPhone(),
                hi.getNote(),
                hi.getIsActive(),
                hi.getPlans().stream()
                        .map(plan -> new InsurancePlanDTO(plan.getId(), plan.getName()))
                        .toList()
        );
    }

    @Override
    public List<HealthInsuranceResponseDTO> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(hi -> new HealthInsuranceResponseDTO(
                        hi.getId(),
                        hi.getName(),
                        hi.getContactEmail(),
                        hi.getPhone(),
                        hi.getNote(),
                        hi.getIsActive(),
                        hi.getPlans().stream()
                                .map(plan -> new InsurancePlanDTO(plan.getId(), plan.getName()))
                                .toList()
                ))
                .toList();
    }

    @Override
    public List<HealthInsuranceResponseDTO> findAllActive() {
        return repository.findAllByIsActiveTrue()
                .stream()
                .map(hi -> new HealthInsuranceResponseDTO(
                        hi.getId(),
                        hi.getName(),
                        hi.getContactEmail(),
                        hi.getPhone(),
                        hi.getNote(),
                        hi.getIsActive(),
                        hi.getPlans().stream()
                                .map(plan -> new InsurancePlanDTO(plan.getId(), plan.getName()))
                                .toList()
                ))
                .toList();
    }

    @Override
    public HealthInsuranceResponseDTO findById(Long id) {
        HealthInsurance hi = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Obra social no encontrada"));

        return new HealthInsuranceResponseDTO(
                hi.getId(),
                hi.getName(),
                hi.getContactEmail(),
                hi.getPhone(),
                hi.getNote(),
                hi.getIsActive(),
                hi.getPlans().stream()
                        .map(plan -> new InsurancePlanDTO(plan.getId(), plan.getName()))
                        .toList()
        );
    }



}

