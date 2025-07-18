package com.martinez.dentist.professionals.services;

import com.martinez.dentist.professionals.controllers.professionalSpecialty.ProfessionalSpecialtyRequestDTO;
import com.martinez.dentist.professionals.controllers.professionalSpecialty.ProfessionalSpecialtyResponseDTO;
import com.martinez.dentist.professionals.models.Professional;
import com.martinez.dentist.professionals.models.ProfessionalSpecialty;
import com.martinez.dentist.professionals.models.Specialty;
import com.martinez.dentist.professionals.repositories.ProfessionalRepository;
import com.martinez.dentist.professionals.repositories.ProfessionalSpecialtyRepository;
import com.martinez.dentist.professionals.repositories.SpecialtyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProfessionalSpecialtyServiceImpl implements ProfessionalSpecialtyService {

    private final ProfessionalSpecialtyRepository repository;
    private final ProfessionalRepository professionalRepository;
    private final SpecialtyRepository specialtyRepository;

    public ProfessionalSpecialtyServiceImpl(ProfessionalSpecialtyRepository repository,
                                            ProfessionalRepository professionalRepository,
                                            SpecialtyRepository specialtyRepository) {
        this.repository = repository;
        this.professionalRepository = professionalRepository;
        this.specialtyRepository = specialtyRepository;
    }

    @Override
    public ProfessionalSpecialtyResponseDTO assignSpecialty(ProfessionalSpecialtyRequestDTO dto) {
        if (repository.existsByProfessionalIdAndSpecialtyId(dto.getProfessionalId(), dto.getSpecialtyId())) {
            throw new IllegalArgumentException("Este profesional ya tiene esta especialidad asignada.");
        }

        Professional professional = professionalRepository.findById(dto.getProfessionalId())
                .orElseThrow(() -> new IllegalArgumentException("Profesional no encontrado"));

        Specialty specialty = specialtyRepository.findById(dto.getSpecialtyId())
                .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada"));

        ProfessionalSpecialty ps = new ProfessionalSpecialty();
        ps.setProfessional(professional);
        ps.setSpecialty(specialty);

        repository.save(ps);

        return new ProfessionalSpecialtyResponseDTO(
                ps.getId(),
                professional.getId(),
                professional.getFullName(),
                specialty.getId(),
                specialty.getName()
        );
    }

    @Override
    public List<ProfessionalSpecialtyResponseDTO> getSpecialtiesByProfessional(Long professionalId) {
        return repository.findByProfessionalId(professionalId).stream()
                .map(ps -> new ProfessionalSpecialtyResponseDTO(
                        ps.getId(),
                        ps.getProfessional().getId(),
                        ps.getProfessional().getFullName(),
                        ps.getSpecialty().getId(),
                        ps.getSpecialty().getName()
                )).collect(Collectors.toList());
    }

    @Override
    public void removeSpecialtyByProfessionalAndSpecialty(Long professionalId, Long specialtyId) {
        if (!repository.existsByProfessionalIdAndSpecialtyId(professionalId, specialtyId)) {
            throw new IllegalArgumentException("El profesional no tiene asignada esa especialidad.");
        }
        repository.deleteByProfessionalIdAndSpecialtyId(professionalId, specialtyId);
    }
}
