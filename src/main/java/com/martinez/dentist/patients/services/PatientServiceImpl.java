package com.martinez.dentist.patients.services;

import com.martinez.dentist.exceptions.NoChangesDetectedException;
import com.martinez.dentist.patients.models.HealthInsurance;
import com.martinez.dentist.patients.models.InsurancePlan;
import com.martinez.dentist.patients.repositories.HealthInsuranceRepository;
import com.martinez.dentist.patients.repositories.InsurancePlanRepository;
import com.martinez.dentist.patients.controllers.PatientRequestDTO;
import com.martinez.dentist.patients.controllers.PatientResponseDTO;
import com.martinez.dentist.patients.models.Patient;
import com.martinez.dentist.patients.models.PatientState;
import com.martinez.dentist.patients.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository repository;

    @Autowired
    private HealthInsuranceRepository healthInsuranceRepository;

    @Autowired
    private InsurancePlanRepository insurancePlanRepository;

    @Override
    public String save(PatientRequestDTO dto) {
        Optional<Patient> existing = repository.findByDocumentNumber(dto.getDocumentNumber());
        if (existing.isPresent()) {
            throw new RuntimeException("Ya existe un paciente con ese DNI");
        }

        InsurancePlan plan = insurancePlanRepository.findById(dto.getInsurancePlanId())
                .orElseThrow(() -> new RuntimeException("Plan no encontrado"));

        HealthInsurance healthInsurance = healthInsuranceRepository.findById(dto.getHealthInsuranceId())
                .orElseThrow(() -> new RuntimeException("Obra social no encontrada"));

        if (!plan.getHealthInsurance().getId().equals(healthInsurance.getId())) {
            throw new RuntimeException("El plan no corresponde a la obra social seleccionada");
        }

        Patient patient = new Patient(
                dto.getFullName(),
                dto.getDocumentType(),
                dto.getDocumentNumber(),
                healthInsurance,
                plan,
                dto.getAffiliateNumber(),
                dto.getPhone(),
                dto.getEmail(),
                dto.getRegistrationDate(),
                dto.getLastVisitDate(),
                dto.getNote()
        );

        repository.save(patient);
        return "Paciente creado con éxito.";
    }


    @Override
    public PatientResponseDTO findById(Long id) {
        Patient patient = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        return toResponseDTO(patient);
    }

    @Override
    public List<PatientResponseDTO> findAll() {
        return ((List<Patient>) repository.findAll()).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Override
    public List<PatientResponseDTO> findByState(PatientState state) {
        return repository.findByPatientState(state).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Override
    public PatientResponseDTO update(Long id, PatientRequestDTO dto) {
        Patient patient = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        InsurancePlan plan = insurancePlanRepository.findById(dto.getInsurancePlanId())
                .orElseThrow(() -> new RuntimeException("Plan no encontrado"));

        HealthInsurance healthInsurance = healthInsuranceRepository.findById(dto.getHealthInsuranceId())
                .orElseThrow(() -> new RuntimeException("Obra social no encontrada"));

        if (!plan.getHealthInsurance().getId().equals(healthInsurance.getId())) {
            throw new RuntimeException("El plan no corresponde a la obra social seleccionada");
        }

        boolean noChanges =
                patient.getFullName().equals(dto.getFullName()) &&
                        patient.getDocumentType().equals(dto.getDocumentType()) &&
                        patient.getDocumentNumber().equals(dto.getDocumentNumber()) &&
                        patient.getAffiliateNumber().equals(dto.getAffiliateNumber()) &&
                        patient.getPhone().equals(dto.getPhone()) &&
                        patient.getEmail().equals(dto.getEmail()) &&
                        patient.getNote().equals(dto.getNote()) &&
                        patient.getInsurancePlan().getId().equals(plan.getId()) &&
                        patient.getHealthInsurance().getId().equals(healthInsurance.getId());

        if (noChanges) {
            throw new NoChangesDetectedException("No se detectaron cambios en los datos del paciente.");
        }

        patient.updateData(dto, healthInsurance, plan);
        repository.save(patient);

        return toResponseDTO(patient);
    }


    @Override
    public void disable(Long id) {
        Patient patient = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        patient.disablePatient();
        repository.save(patient);
    }

    @Override
    public void enable(Long id) {
        Patient patient = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        patient.enablePatient();
        repository.save(patient);
    }

    private PatientResponseDTO toResponseDTO(Patient patient) {
        return new PatientResponseDTO(
                patient.getId(),
                patient.getFullName(),
                patient.getDocumentType(),
                patient.getDocumentNumber(),

                patient.getHealthInsurance() != null ? patient.getHealthInsurance().getId() : null,
                patient.getHealthInsurance() != null ? patient.getHealthInsurance().getName() : null,

                patient.getInsurancePlan() != null ? patient.getInsurancePlan().getId() : null,
                patient.getInsurancePlan() != null ? patient.getInsurancePlan().getName() : null,

                patient.getAffiliateNumber(),
                patient.getPhone(),
                patient.getEmail(),
                patient.getRegistrationDate(),
                patient.getLastVisitDate(),
                patient.getNote(),
                patient.getPatientState().getDisplayName()
        );
    }
}
