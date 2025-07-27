package com.martinez.dentist.patients.services;

import com.martinez.dentist.patients.controllers.InsurancePlan.InsurancePlanRequestDTO;
import com.martinez.dentist.patients.controllers.InsurancePlan.InsurancePlanResponseDTO;
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
    public InsurancePlanResponseDTO update(Long id, InsurancePlanRequestDTO dto) {
        InsurancePlan plan = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan no encontrado"));

        boolean hasChanges = false;

        if (!plan.getName().equals(dto.getName())) {
            plan.setName(dto.getName());
            hasChanges = true;
        }

        if (!plan.getHealthInsurance().getId().equals(dto.getHealthInsuranceId())) {
            HealthInsurance newHI = healthInsuranceRepository.findById(dto.getHealthInsuranceId())
                    .orElseThrow(() -> new RuntimeException("Obra social no encontrada"));
            plan.setHealthInsurance(newHI);
            hasChanges = true;
        }

        if (!hasChanges) {
            throw new RuntimeException("No se detectaron cambios para actualizar");
        }

        planRepository.save(plan);

        return toResponseDTO(plan);
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
