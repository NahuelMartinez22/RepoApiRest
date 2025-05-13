package com.martinez.dentist.patients.services;

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

        HealthInsurance healthInsurance = healthInsuranceRepository.findById(dto.getHealthInsuranceId())
                .orElseThrow(() -> new RuntimeException("Obra social no encontrada"));

        InsurancePlan insurancePlan = insurancePlanRepository.findById(dto.getInsurancePlanId())
                .orElseThrow(() -> new RuntimeException("Plan de obra social no encontrado"));

        Patient patient = new Patient(
                dto.getFullName(),
                dto.getDocumentType(),
                dto.getDocumentNumber(),
                healthInsurance,
                insurancePlan,
                dto.getAffiliateNumber(),
                dto.getPhone(),
                dto.getEmail(),
                dto.getRegistrationDate(),
                dto.getLastVisitDate(),
                dto.getNote()
        );

        repository.save(patient);
        return "El paciente se creó con éxito.";
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

        HealthInsurance healthInsurance = healthInsuranceRepository.findById(dto.getHealthInsuranceId())
                .orElseThrow(() -> new RuntimeException("Obra social no encontrada"));

        InsurancePlan insurancePlan = insurancePlanRepository.findById(dto.getInsurancePlanId())
                .orElseThrow(() -> new RuntimeException("Plan de obra social no encontrado"));

        patient.updateData(dto, healthInsurance, insurancePlan);
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
                patient.getHealthInsurance() != null ? patient.getHealthInsurance().getName() : null,
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
