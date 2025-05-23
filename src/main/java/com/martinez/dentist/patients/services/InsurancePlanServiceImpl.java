package com.martinez.dentist.patients.services;

import com.martinez.dentist.patients.controllers.InsurancePlanRequestDTO;
import com.martinez.dentist.patients.controllers.InsurancePlanResponseDTO;
import com.martinez.dentist.patients.models.HealthInsurance;
import com.martinez.dentist.patients.models.InsurancePlan;
import com.martinez.dentist.patients.repositories.HealthInsuranceRepository;
import com.martinez.dentist.patients.repositories.InsurancePlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class InsurancePlanServiceImpl implements InsurancePlanService {

    @Autowired
    private InsurancePlanRepository planRepository;

    @Autowired
    private HealthInsuranceRepository healthInsuranceRepository;

    @Override
    public String create(InsurancePlanRequestDTO dto) {
        HealthInsurance hi = healthInsuranceRepository.findById(dto.getHealthInsuranceId())
                .orElseThrow(() -> new RuntimeException("Obra social no encontrada"));

        InsurancePlan plan = new InsurancePlan(dto.getName(), hi);
        planRepository.save(plan);
        return "Plan creado con éxito.";
    }

    @Override
    public List<InsurancePlanResponseDTO> getAll() {
        return StreamSupport.stream(planRepository.findAll().spliterator(), false)
                .map(this::toResponseDTO)
                .toList();
    }

    @Override
    public List<InsurancePlanResponseDTO> getByHealthInsuranceId(Long id) {
        return planRepository.findByHealthInsuranceId(id).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private InsurancePlanResponseDTO toResponseDTO(InsurancePlan plan) {
        return new InsurancePlanResponseDTO(
                plan.getId(),
                plan.getName(),
                plan.getHealthInsurance().getName()
        );
    }

    @Override
    public void deleteById(Long id) {
        if (!planRepository.existsById(id)) {
            throw new RuntimeException("El plan con ID " + id + " no existe.");
        }
        planRepository.deleteById(id);
    }
}
