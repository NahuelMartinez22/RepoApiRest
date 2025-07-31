package com.martinez.dentist.patients.services;

import com.martinez.dentist.appointments.repositories.AppointmentRepository;
import com.martinez.dentist.exceptions.NoChangesDetectedException;
import com.martinez.dentist.patients.controllers.PatientRequestDTO;
import com.martinez.dentist.patients.controllers.PatientResponseDTO;
import com.martinez.dentist.patients.models.*;
import com.martinez.dentist.patients.repositories.HealthInsuranceRepository;
import com.martinez.dentist.patients.repositories.InsurancePlanRepository;
import com.martinez.dentist.patients.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository repository;

    @Autowired
    private HealthInsuranceRepository healthInsuranceRepository;

    @Autowired
    private InsurancePlanRepository insurancePlanRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public PatientResponseDTO save(PatientRequestDTO dto) {
        if (repository.findByDocumentNumber(dto.getDocumentNumber()).isPresent()) {
            throw new RuntimeException("Ya existe un paciente con ese DNI");
        }

        validatePatientDTO(dto);

        InsurancePlan plan = getPlan(dto);
        HealthInsurance healthInsurance = getHealthInsurance(dto);

        if (plan != null && healthInsurance != null &&
                !Objects.equals(plan.getHealthInsurance().getId(), healthInsurance.getId())) {
            throw new RuntimeException("El plan no corresponde a la obra social seleccionada");
        }

        String trimmedAffiliate = dto.getAffiliateNumber() != null ? dto.getAffiliateNumber().trim() : null;

        Patient patient = new Patient(
                dto.getFullName().trim(),
                dto.getDocumentType().trim(),
                dto.getDocumentNumber().trim(),
                healthInsurance,
                plan,
                trimmedAffiliate,
                dto.getPhone(),
                dto.getEmail(),
                dto.getRegistrationDate(),
                dto.getLastVisitDate(),
                dto.getNote()
        );

        Patient savedPatient = repository.save(patient);

        return toResponseDTO(savedPatient);
    }

    @Override
    public PatientResponseDTO update(Long id, PatientRequestDTO dto) {
        Patient patient = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        validatePatientDTO(dto);

        InsurancePlan plan = getPlan(dto);
        HealthInsurance healthInsurance = getHealthInsurance(dto);

        if (plan != null && healthInsurance != null &&
                !Objects.equals(plan.getHealthInsurance().getId(), healthInsurance.getId())) {
            throw new RuntimeException("El plan no corresponde a la obra social seleccionada");
        }

        String trimmedAffiliate = dto.getAffiliateNumber() != null ? dto.getAffiliateNumber().trim() : null;

        if (!Objects.equals(patient.getDocumentNumber(), dto.getDocumentNumber())) {
            boolean tieneTurnos = appointmentRepository.existsByPatientDni(patient.getDocumentNumber());
            if (tieneTurnos) {
                throw new RuntimeException("No se puede cambiar el DNI porque el paciente tiene turnos asociados.");
            }
        }

        boolean noChanges =
                Objects.equals(patient.getFullName(), dto.getFullName()) &&
                        Objects.equals(patient.getDocumentType(), dto.getDocumentType()) &&
                        Objects.equals(patient.getDocumentNumber(), dto.getDocumentNumber()) &&
                        Objects.equals(patient.getAffiliateNumber(), trimmedAffiliate) &&
                        Objects.equals(patient.getPhone(), dto.getPhone()) &&
                        Objects.equals(patient.getEmail(), dto.getEmail()) &&
                        Objects.equals(patient.getNote(), dto.getNote()) &&
                        ((patient.getInsurancePlan() == null && plan == null) ||
                                (patient.getInsurancePlan() != null && plan != null &&
                                        Objects.equals(patient.getInsurancePlan().getId(), plan.getId()))) &&
                        ((patient.getHealthInsurance() == null && healthInsurance == null) ||
                                (patient.getHealthInsurance() != null && healthInsurance != null &&
                                        Objects.equals(patient.getHealthInsurance().getId(), healthInsurance.getId())));

        if (noChanges) {
            throw new NoChangesDetectedException("No se detectaron cambios en los datos del paciente.");
        }

        patient.updateData(dto, healthInsurance, plan);
        repository.save(patient);

        return toResponseDTO(patient);
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

    private void validatePatientDTO(PatientRequestDTO dto) {
        if (dto.getFullName() == null || dto.getFullName().trim().isEmpty()) {
            throw new RuntimeException("El nombre completo es obligatorio");
        }

        if (dto.getDocumentType() == null || dto.getDocumentType().trim().isEmpty()) {
            throw new RuntimeException("El tipo de documento es obligatorio");
        }

        if (dto.getDocumentNumber() == null || dto.getDocumentNumber().trim().isEmpty()) {
            throw new RuntimeException("El número de documento es obligatorio");
        }

        String trimmedAffiliate = dto.getAffiliateNumber() != null ? dto.getAffiliateNumber().trim() : null;

        if (trimmedAffiliate != null && !trimmedAffiliate.isEmpty()) {
            if (dto.getHealthInsuranceId() == null || dto.getInsurancePlanId() == null) {
                throw new RuntimeException("Debe seleccionar obra social y plan si se proporciona un número de afiliado.");
            }
        }

        if (dto.getHealthInsuranceId() != null && dto.getInsurancePlanId() != null) {
            if (trimmedAffiliate == null || trimmedAffiliate.isEmpty()) {
                throw new RuntimeException("Debe ingresar el número de afiliado si selecciona obra social y plan.");
            }
        }
    }

    private InsurancePlan getPlan(PatientRequestDTO dto) {
        if (dto.getInsurancePlanId() != null) {
            return insurancePlanRepository.findById(dto.getInsurancePlanId())
                    .orElseThrow(() -> new RuntimeException("Plan no encontrado"));
        }
        return null;
    }

    private HealthInsurance getHealthInsurance(PatientRequestDTO dto) {
        if (dto.getHealthInsuranceId() != null) {
            return healthInsuranceRepository.findById(dto.getHealthInsuranceId())
                    .orElseThrow(() -> new RuntimeException("Obra social no encontrada"));
        }
        return null;
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
