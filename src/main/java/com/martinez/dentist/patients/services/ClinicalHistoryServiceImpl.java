package com.martinez.dentist.patients.services;

import com.martinez.dentist.patients.controllers.ClinicalFileDTO;
import com.martinez.dentist.patients.controllers.ClinicalHistoryRequestDTO;
import com.martinez.dentist.patients.controllers.ClinicalHistoryResponseDTO;
import com.martinez.dentist.patients.models.ClinicalHistory;
import com.martinez.dentist.patients.repositories.ClinicalHistoryRepository;
import com.martinez.dentist.patients.models.Patient;
import com.martinez.dentist.patients.repositories.PatientRepository;
import com.martinez.dentist.professionals.models.Professional;
import com.martinez.dentist.professionals.repositories.ProfessionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClinicalHistoryServiceImpl implements ClinicalHistoryService {

    @Autowired
    private ClinicalHistoryRepository clinicalHistoryRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ProfessionalRepository professionalRepository;

    @Override
    public String createClinicalHistory(ClinicalHistoryRequestDTO dto) {
        Patient patient = patientRepository.findByDocumentNumber(dto.getPatientDocumentNumber())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con DNI: " + dto.getPatientDocumentNumber()));

        Professional professional = professionalRepository.findById(dto.getProfessionalId())
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));

        ClinicalHistory clinicalHistory = new ClinicalHistory(
                patient,
                professional,
                dto.getDate(),
                dto.getDescription()
        );

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

            return new ClinicalHistoryResponseDTO(
                    history.getId(),
                    history.getPatient().getFullName(),
                    history.getProfessional().getFullName(),
                    history.getDate(),
                    history.getDescription(),
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
