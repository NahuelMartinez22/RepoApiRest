package com.martinez.dentist.professionals.services;

import com.martinez.dentist.exceptions.NoChangesDetectedException;
import com.martinez.dentist.professionals.controllers.ProfessionalRequestDTO;
import com.martinez.dentist.professionals.controllers.ScheduleRequestDTO;
import com.martinez.dentist.professionals.models.Professional;
import com.martinez.dentist.professionals.models.ProfessionalSchedule;
import com.martinez.dentist.professionals.models.ProfessionalState;
import com.martinez.dentist.professionals.repositories.ProfessionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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
    public Professional updateById(Long id, ProfessionalRequestDTO dto) {
        Professional professional = professionalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));

        boolean personalDataUnchanged =
                Objects.equals(professional.getFullName(), dto.getFullName()) &&
                        Objects.equals(professional.getPhone(), dto.getPhone()) &&
                        Objects.equals(professional.getDocumentType(), dto.getDocumentType()) &&
                        Objects.equals(professional.getDocumentNumber(), dto.getDocumentNumber());

        boolean schedulesUnchanged = schedulesAreEqual(professional.getSchedules(), dto.getSchedules());

        if (personalDataUnchanged && schedulesUnchanged) {
            throw new NoChangesDetectedException("No se detectaron cambios en los datos del profesional.");
        }

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
            ).toList();

            professional.getSchedules().addAll(schedules);
        }

        return professionalRepository.save(professional);
    }

    private boolean schedulesAreEqual(List<ProfessionalSchedule> current, List<ScheduleRequestDTO> incoming) {
        if (incoming == null || current == null || current.size() != incoming.size()) {
            return false;
        }

        List<ProfessionalSchedule> currentSorted = current.stream()
                .sorted(Comparator.comparing(ProfessionalSchedule::getDayOfWeek)
                        .thenComparing(ProfessionalSchedule::getStartTime))
                .toList();

        List<ScheduleRequestDTO> incomingSorted = incoming.stream()
                .sorted(Comparator.comparing(ScheduleRequestDTO::getDayOfWeek)
                        .thenComparing(ScheduleRequestDTO::getStartTime))
                .toList();

        for (int i = 0; i < currentSorted.size(); i++) {
            ProfessionalSchedule c = currentSorted.get(i);
            ScheduleRequestDTO d = incomingSorted.get(i);

            if (!Objects.equals(c.getDayOfWeek(), d.getDayOfWeek()) ||
                    !Objects.equals(c.getStartTime(), d.getStartTime()) ||
                    !Objects.equals(c.getEndTime(), d.getEndTime())) {
                return false;
            }
        }

        return true;
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
