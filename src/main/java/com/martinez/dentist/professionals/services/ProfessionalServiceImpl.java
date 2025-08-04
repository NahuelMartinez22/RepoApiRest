package com.martinez.dentist.professionals.services;

import com.martinez.dentist.exceptions.NoChangesDetectedException;
import com.martinez.dentist.professionals.controllers.professional.ProfessionalRequestDTO;
import com.martinez.dentist.professionals.controllers.professional.ProfessionalResponseDTO;
import com.martinez.dentist.professionals.controllers.schedule.ScheduleRequestDTO;
import com.martinez.dentist.professionals.controllers.schedule.ScheduleResponseDTO;
import com.martinez.dentist.professionals.models.*;
import com.martinez.dentist.professionals.repositories.ProfessionalRepository;
import com.martinez.dentist.professionals.repositories.SpecialtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProfessionalServiceImpl implements ProfessionalService {

    @Autowired
    private ProfessionalRepository professionalRepository;

    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Override
    public Long create(ProfessionalRequestDTO dto) {
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
            List<ProfessionalSchedule> schedules = dto.getSchedules().stream()
                    .map(scheduleDTO -> new ProfessionalSchedule(
                            professional,
                            scheduleDTO.getDayOfWeek(),
                            scheduleDTO.getStartTime(),
                            scheduleDTO.getEndTime()
                    ))
                    .collect(Collectors.toList());
            professional.getSchedules().addAll(schedules);
        }

        if (dto.getSpecialtyIds() != null && !dto.getSpecialtyIds().isEmpty()) {
            Set<Long> uniqueSpecIds = new HashSet<>(dto.getSpecialtyIds());
            if (uniqueSpecIds.size() < dto.getSpecialtyIds().size()) {
                throw new RuntimeException("Hay IDs de especialidades duplicados en el request.");
            }

            for (Long specId : uniqueSpecIds) {
                Specialty specialty = specialtyRepository.findById(specId)
                        .orElseThrow(() -> new RuntimeException("Especialidad no encontrada con ID: " + specId));
                ProfessionalSpecialty ps = new ProfessionalSpecialty();
                ps.setProfessional(professional);
                ps.setSpecialty(specialty);
                professional.getProfessionalSpecialties().add(ps);
            }
        }

        Professional saved = professionalRepository.save(professional);
        return saved.getId();
    }

    @Override
    public Long updateById(Long id, ProfessionalRequestDTO dto) {
        Professional professional = professionalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));

        boolean personalDataUnchanged =
                Objects.equals(professional.getFullName(), dto.getFullName()) &&
                        Objects.equals(professional.getPhone(), dto.getPhone()) &&
                        Objects.equals(professional.getDocumentType(), dto.getDocumentType()) &&
                        Objects.equals(professional.getDocumentNumber(), dto.getDocumentNumber());

        boolean schedulesUnchanged = schedulesAreEqual(professional.getSchedules(), dto.getSchedules());

        List<Long> newSpecialtyIds = dto.getSpecialtyIds() != null
                ? dto.getSpecialtyIds()
                : List.of();
        Set<Long> currentSpecialtyIds = professional.getProfessionalSpecialties().stream()
                .map(ps -> ps.getSpecialty().getId())
                .collect(Collectors.toSet());
        boolean specialtiesUnchanged = new HashSet<>(newSpecialtyIds).equals(currentSpecialtyIds);

        if (personalDataUnchanged && schedulesUnchanged && specialtiesUnchanged) {
            throw new NoChangesDetectedException("No se detectaron cambios en los datos del profesional.");
        }

        professional.updateData(dto);

        if (dto.getSchedules() != null) {
            professional.getSchedules().clear();
            List<ProfessionalSchedule> schedules = dto.getSchedules().stream()
                    .map(sd -> new ProfessionalSchedule(
                            professional,
                            sd.getDayOfWeek(),
                            sd.getStartTime(),
                            sd.getEndTime()))
                    .toList();
            professional.getSchedules().addAll(schedules);
        }

        if (dto.getSpecialtyIds() != null) {
            professional.getProfessionalSpecialties().clear();
            for (Long specId : newSpecialtyIds) {
                Specialty specialty = specialtyRepository.findById(specId)
                        .orElseThrow(() -> new RuntimeException("Especialidad no encontrada con ID: " + specId));
                ProfessionalSpecialty ps = new ProfessionalSpecialty();
                ps.setProfessional(professional);
                ps.setSpecialty(specialty);
                professional.getProfessionalSpecialties().add(ps);
            }
        }

        Professional saved = professionalRepository.save(professional);
        return saved.getId();
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
    public List<ProfessionalResponseDTO> findAll() {
        return StreamSupport.stream(professionalRepository.findAll().spliterator(), false)
                .map(this::toResponseDTO)
                .toList();
    }

    @Override
    public List<ProfessionalResponseDTO> findAllActive() {
        return professionalRepository.findAllByProfessionalState(ProfessionalState.ACTIVE).stream()
                .map(this::toResponseDTO)
                .toList();
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

    private ProfessionalResponseDTO toResponseDTO(Professional professional) {
        List<ScheduleResponseDTO> scheduleDTOs = professional.getSchedules().stream()
                .map(schedule -> new ScheduleResponseDTO(
                        schedule.getDayOfWeek(),
                        schedule.getStartTime(),
                        schedule.getEndTime()
                ))
                .toList();

        List<Long> specialtyIds = professional.getProfessionalSpecialties().stream()
                .map(ps -> ps.getSpecialty().getId())
                .distinct()
                .toList();

        List<String> specialtyNames = professional.getProfessionalSpecialties().stream()
                .map(ps -> ps.getSpecialty().getName())
                .distinct()
                .toList();

        return new ProfessionalResponseDTO(
                professional.getId(),
                professional.getFullName(),
                professional.getDocumentType(),
                professional.getDocumentNumber(),
                professional.getPhone(),
                scheduleDTOs,
                specialtyIds,
                specialtyNames,
                professional.getProfessionalState().toString(),
                professional.getAvailable()
        );
    }

    @Override
    public void setAvailable(Long professionalId, boolean available) {
        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado con ID: " + professionalId));

        if (professional.getProfessionalState() == ProfessionalState.DEACTIVATED) {
            throw new RuntimeException("No se puede modificar la disponibilidad de un profesional desactivado.");
        }

        if (professional.getAvailable().equals(available)) {
            throw new RuntimeException("El profesional ya tiene el estado de disponibilidad solicitado.");
        }

        professional.setAvailable(available);
        professionalRepository.save(professional);
    }

    @Override
    public List<ProfessionalResponseDTO> getAvailableProfessionals() {
        return professionalRepository.findByAvailableTrue().stream()
                .filter(p -> p.getProfessionalState() == ProfessionalState.ACTIVE)
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void assignLicenseDates(Long professionalId, LocalDate startDate, LocalDate endDate) {
        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado con ID: " + professionalId));

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Las fechas de licencia no pueden ser nulas");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior o igual a la fecha de fin");
        }

        if (Objects.equals(professional.getLicenseStartDate(), startDate) &&
                Objects.equals(professional.getLicenseEndDate(),   endDate)) {
            throw new NoChangesDetectedException("No hubo cambios en las fechas de licencia");
        }

        professional.setLicenseStartDate(startDate);
        professional.setLicenseEndDate(endDate);
        professionalRepository.save(professional);
    }

}
