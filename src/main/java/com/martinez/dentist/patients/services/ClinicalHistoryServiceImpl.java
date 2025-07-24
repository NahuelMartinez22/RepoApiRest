package com.martinez.dentist.patients.services;

import com.martinez.dentist.exceptions.NoChangesDetectedException;
import com.martinez.dentist.patients.controllers.ClinicalFileDTO;
import com.martinez.dentist.patients.controllers.ClinicalHistoryRequestDTO;
import com.martinez.dentist.patients.controllers.ClinicalHistoryResponseDTO;
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
import java.util.List;
import java.util.Objects;
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

        List<DentalProcedure> procedures = dto.getProcedureIds().stream()
                .map(id -> dentalProcedureRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Procedimiento no encontrado con ID: " + id)))
                .toList();

        ClinicalHistory clinicalHistory = new ClinicalHistory(
                patient,
                professional,
                LocalDateTime.now(),
                dto.getDescription()
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
    public String updateClinicalHistory(Long id, ClinicalHistoryRequestDTO dto) {
        ClinicalHistory clinicalHistory = clinicalHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada"));

        Professional professional = professionalRepository.findById(dto.getProfessionalId())
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));

        List<DentalProcedure> newProcedures = dto.getProcedureIds().stream()
                .map(pid -> dentalProcedureRepository.findById(pid)
                        .orElseThrow(() -> new RuntimeException("Procedimiento no encontrado con ID: " + pid)))
                .toList();

        boolean sinCambios =
                Objects.equals(clinicalHistory.getProfessional().getId(), dto.getProfessionalId()) &&
                        Objects.equals(
                                clinicalHistory.getProcedures().stream().map(DentalProcedure::getId).sorted().toList(),
                                newProcedures.stream().map(DentalProcedure::getId).sorted().toList()
                        ) &&
                        Objects.equals(clinicalHistory.getDescription(), dto.getDescription());

        if (sinCambios) {
            throw new NoChangesDetectedException("No se detectaron cambios en la historia clínica.");
        }

        clinicalHistory.setProfessional(professional);
        clinicalHistory.setProcedures(newProcedures);
        clinicalHistory.setDescription(dto.getDescription());

        clinicalHistoryRepository.save(clinicalHistory);
        return "Historia clínica actualizada correctamente.";
    }

    @Override
    public String addProcedureToClinicalHistory(Long historyId, Long procedureId) {
        ClinicalHistory history = clinicalHistoryRepository.findById(historyId)
                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada con ID: " + historyId));

        DentalProcedure procedure = dentalProcedureRepository.findById(procedureId)
                .orElseThrow(() -> new RuntimeException("Procedimiento no encontrado con ID: " + procedureId));

        boolean yaExiste = history.getProcedures().stream()
                .anyMatch(p -> p.getId().equals(procedureId));
        if (yaExiste) {
            return "El procedimiento ya está registrado en la historia clínica.";
        }

        history.addProcedure(procedure);
        clinicalHistoryRepository.save(history);
        return "Procedimiento agregado a la historia clínica.";
    }

    @Override
    public String removeProcedureFromClinicalHistory(Long historyId, Long procedureId) {
        ClinicalHistory history = clinicalHistoryRepository.findById(historyId)
                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada con ID: " + historyId));

        DentalProcedure procedure = dentalProcedureRepository.findById(procedureId)
                .orElseThrow(() -> new RuntimeException("Procedimiento no encontrado con ID: " + procedureId));

        boolean existia = history.getProcedures().removeIf(p -> p.getId().equals(procedureId));
        if (!existia) {
            return "El procedimiento no estaba asignado a la historia clínica.";
        }

        history.removeProcedure(procedure);
        clinicalHistoryRepository.save(history);
        return "Procedimiento eliminado de la historia clínica.";
    }
}
