package com.martinez.dentist.patients.services;

import com.martinez.dentist.patients.controllers.ClinicalFileDTO;
import com.martinez.dentist.patients.controllers.ClinicalHistoryRequestDTO;
import com.martinez.dentist.patients.controllers.ClinicalHistoryResponseDTO;
import com.martinez.dentist.patients.models.ClinicalHistory;
import com.martinez.dentist.patients.models.DentalProcedure;
import com.martinez.dentist.patients.repositories.ClinicalHistoryRepository;
import com.martinez.dentist.patients.models.Patient;
import com.martinez.dentist.patients.repositories.DentalProcedureRepository;
import com.martinez.dentist.patients.repositories.PatientRepository;
import com.martinez.dentist.professionals.models.Professional;
import com.martinez.dentist.professionals.repositories.ProfessionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

        DentalProcedure procedure = dentalProcedureRepository.findById(dto.getProcedureId())
                .orElseThrow(() -> new RuntimeException("Procedimiento no encontrado"));

        ClinicalHistory clinicalHistory = new ClinicalHistory(
                patient,
                professional,
                LocalDate.now(),
                dto.getDescription()
        );
        clinicalHistory.setProcedure(procedure);

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

            Long procedureId = history.getProcedure().getId();
            String procedureName = history.getProcedure().getName();
            String description = history.getDescription();

            return new ClinicalHistoryResponseDTO(
                    history.getId(),
                    history.getPatient().getFullName(),
                    history.getProfessional().getFullName(),
                    history.getDate(),
                    description,
                    procedureId,
                    procedureName,
                    fileDTOs
            );
        }).toList();
    }

        @Override
        public ClinicalHistory getById(Long id) {
            return clinicalHistoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada"));
        }
    }
