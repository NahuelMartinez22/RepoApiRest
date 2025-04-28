package com.martinez.dentist.professionals.services;

import com.martinez.dentist.professionals.controllers.ProfessionalRequestDTO;
import com.martinez.dentist.professionals.controllers.ScheduleRequestDTO;
import com.martinez.dentist.professionals.models.Professional;
import com.martinez.dentist.professionals.models.ProfessionalSchedule;
import com.martinez.dentist.professionals.models.ProfessionalState;
import com.martinez.dentist.professionals.repositories.ProfessionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfessionalServiceImpl implements ProfessionalService {

    @Autowired
    private ProfessionalRepository professionalRepository;

    @Override
    public Professional create(ProfessionalRequestDTO dto) {
        if (professionalRepository.findByDocumentNumber(dto.getDocumentNumber()).isPresent()) {
            throw new RuntimeException("Ya existe un profesional con el mismo número de documento.");
        }

        Professional professional = new Professional(
                dto.getFullName(),
                dto.getDocumentType(),
                dto.getDocumentNumber(),
                dto.getPhone()
        );

        if (dto.getSchedules() != null && !dto.getSchedules().isEmpty()) {
            List<ProfessionalSchedule> schedules = dto.getSchedules().stream().map(scheduleDTO ->
                    new ProfessionalSchedule(
                            professional,
                            scheduleDTO.getDayOfWeek(),
                            scheduleDTO.getStartTime(),
                            scheduleDTO.getEndTime()
                    )
            ).collect(Collectors.toList());

            professional.getSchedules().addAll(schedules);
        }

        return professionalRepository.save(professional);
    }

    @Override
    public Professional updateByDocumentNumber(String documentNumber, ProfessionalRequestDTO dto) {
        Professional professional = professionalRepository.findByDocumentNumber(documentNumber)
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));

        professional.updateData(dto);

        if (dto.getSchedules() != null) {
            professional.getSchedules().clear();
            List<ProfessionalSchedule> schedules = dto.getSchedules().stream().map(scheduleDTO ->
                    new ProfessionalSchedule(
                            professional,
                            scheduleDTO.getDayOfWeek(),
                            scheduleDTO.getStartTime(),
                            scheduleDTO.getEndTime()
                    )
            ).collect(Collectors.toList());

            professional.getSchedules().addAll(schedules);
        }

        return professionalRepository.save(professional);
    }

    @Override
    public List<Professional> findAll() {
        return (List<Professional>) professionalRepository.findAll();
    }

    @Override
    public List<Professional> findAllActive() {
        return professionalRepository.findAllByProfessionalState(ProfessionalState.ACTIVE);
    }

    @Override
    public Professional disableByDocumentNumber(String documentNumber) {
        Professional professional = professionalRepository.findByDocumentNumber(documentNumber)
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));

        professional.disable();
        return professionalRepository.save(professional);
    }

    @Override
    public Professional enableByDocumentNumber(String documentNumber) {
        Professional professional = professionalRepository.findByDocumentNumber(documentNumber)
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));

        professional.enable();
        return professionalRepository.save(professional);
    }
}
