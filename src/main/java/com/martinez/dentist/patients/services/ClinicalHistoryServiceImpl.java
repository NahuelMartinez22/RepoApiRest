package com.martinez.dentist.patients.services;

import com.martinez.dentist.exceptions.NoChangesDetectedException;
import com.martinez.dentist.patients.controllers.clinicalHistory.ClinicalFileDTO;
import com.martinez.dentist.patients.controllers.clinicalHistory.ClinicalHistoryRequestDTO;
import com.martinez.dentist.patients.controllers.clinicalHistory.ClinicalHistoryResponseDTO;
import com.martinez.dentist.patients.models.ClinicalHistory;
import com.martinez.dentist.patients.models.DentalProcedure;
import com.martinez.dentist.patients.models.Patient;
import com.martinez.dentist.patients.repositories.ClinicalHistoryRepository;
import com.martinez.dentist.patients.repositories.DentalProcedureRepository;
import com.martinez.dentist.patients.repositories.PatientRepository;
import com.martinez.dentist.professionals.models.Professional;
import com.martinez.dentist.professionals.repositories.ProfessionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClinicalHistoryServiceImpl implements ClinicalHistoryService {

    @Autowired
    private ClinicalHistoryRepository clinicalHistoryRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ProfessionalRepository professionalRepository;

    @Autowired
    private DentalProcedureRepository dentalProcedureRepository;

    @Override
    public String createClinicalHistory(ClinicalHistoryRequestDTO dto) {
        Patient patient = patientRepository.findByDocumentNumber(dto.getPatientDocumentNumber())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con DNI: " + dto.getPatientDocumentNumber()));

        Professional professional = professionalRepository.findById(dto.getProfessionalId())
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));

        if (dto.getProcedureIds() == null || dto.getProcedureIds().isEmpty()) {
            throw new RuntimeException("Debe ingresar al menos un procedimiento.");
        }

        Set<Long> uniqueIds = new HashSet<>(dto.getProcedureIds());
        if (uniqueIds.size() < dto.getProcedureIds().size()) {
            throw new RuntimeException("Hay procedimientos duplicados en el request.");
        }

        List<DentalProcedure> procedures = uniqueIds.stream()
                .map(id -> dentalProcedureRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Procedimiento no encontrado con ID: " + id)))
                .toList();

        ClinicalHistory clinicalHistory = new ClinicalHistory(
                patient,
                professional,
                LocalDateTime.now(),
                dto.getDescription() != null ? dto.getDescription().trim() : null
        );

        clinicalHistory.setProcedures(procedures);
        clinicalHistoryRepository.save(clinicalHistory);

        return "Historia clínica creada correctamente.";
    }


    @Override
    public List<ClinicalHistoryResponseDTO> getClinicalHistoryByPatient(String documentNumber) {
        Patient patient = patientRepository.findByDocumentNumber(documentNumber)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con DNI: " + documentNumber));

        List<ClinicalHistory> histories = clinicalHistoryRepository.findByPatientId(patient.getId());

        return histories.stream().map(history -> {
            List<ClinicalFileDTO> fileDTOs = history.getFiles().stream()
                    .map(file -> new ClinicalFileDTO(
                            file.getId(),
                            file.getFileName(),
                            file.getFileType()
                    )).toList();

            List<Long> procedureIds = history.getProcedures().stream()
                    .map(DentalProcedure::getId)
                    .toList();

            List<String> procedureNames = history.getProcedures().stream()
                    .map(DentalProcedure::getName)
                    .toList();

            return new ClinicalHistoryResponseDTO(
                    history.getId(),
                    history.getPatient().getFullName(),
                    history.getProfessional().getFullName(),
                    history.getDateTime(),
                    history.getDescription(),
                    procedureIds,
                    procedureNames,
                    fileDTOs
            );
        }).toList();
    }

    @Override
    public ClinicalHistory getById(Long id) {
        return clinicalHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada"));
    }

    @Override
    public void deleteClinicalHistory(Long id) {
        if (!clinicalHistoryRepository.existsById(id)) {
            throw new RuntimeException("Historia clínica no encontrada");
        }
        clinicalHistoryRepository.deleteById(id);
    }

    @Override
    public String updateClinicalHistoryDescription(Long id, ClinicalHistoryRequestDTO dto) {
        ClinicalHistory clinicalHistory = clinicalHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada"));

        if (Objects.equals(clinicalHistory.getDescription(), dto.getDescription())) {
            throw new NoChangesDetectedException("La descripción es la misma, no se detectaron cambios.");
        }

        clinicalHistory.setDescription(dto.getDescription());
        clinicalHistoryRepository.save(clinicalHistory);

        return "Descripción de la historia clínica actualizada correctamente.";
    }

    @Override
    public String addProcedures(Long id, List<Long> procedureIds) {
        ClinicalHistory history = getById(id);

        Set<Long> uniqueRequestIds = new HashSet<>(procedureIds);
        if (uniqueRequestIds.size() < procedureIds.size()) {
            throw new RuntimeException("Hay procedimientos repetidos en el request.");
        }

        Set<Long> currentProcedureIds = history.getProcedures().stream()
                .map(DentalProcedure::getId)
                .collect(Collectors.toSet());

        boolean algunAgregado = false;

        for (Long pid : uniqueRequestIds) {
            if (currentProcedureIds.contains(pid)) continue;

            DentalProcedure procedure = dentalProcedureRepository.findById(pid)
                    .orElseThrow(() -> new RuntimeException("Procedimiento no encontrado con ID: " + pid));

            history.addProcedure(procedure);
            algunAgregado = true;
        }

        if (!algunAgregado) {
            throw new NoChangesDetectedException("Todos los procedimientos ya estaban asociados a la historia clínica.");
        }

        clinicalHistoryRepository.save(history);
        return "Procedimientos agregados correctamente.";
    }


    @Override
    public String removeProcedures(Long id, List<Long> procedureIds) {
        ClinicalHistory history = getById(id);

        Set<Long> uniqueRequestIds = new HashSet<>(procedureIds);
        if (uniqueRequestIds.size() < procedureIds.size()) {
            throw new RuntimeException("Hay procedimientos repetidos en el request.");
        }

        Set<Long> currentProcedureIds = history.getProcedures().stream()
                .map(DentalProcedure::getId)
                .collect(Collectors.toSet());

        boolean algunEliminado = false;

        for (Long pid : uniqueRequestIds) {
            if (!currentProcedureIds.contains(pid)) continue;

            DentalProcedure procedure = dentalProcedureRepository.findById(pid)
                    .orElseThrow(() -> new RuntimeException("Procedimiento no encontrado con ID: " + pid));

            history.removeProcedure(procedure);
            algunEliminado = true;
        }

        if (!algunEliminado) {
            throw new NoChangesDetectedException("Ninguno de los procedimientos estaba asociado a la historia clínica.");
        }

        clinicalHistoryRepository.save(history);
        return "Procedimientos eliminados correctamente.";
    }

}
