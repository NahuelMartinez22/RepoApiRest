package com.martinez.dentist.professionals.services;

import com.martinez.dentist.professionals.controllers.specialty.SpecialtyRequestDTO;
import com.martinez.dentist.professionals.controllers.specialty.SpecialtyResponseDTO;
import com.martinez.dentist.professionals.models.Specialty;
import com.martinez.dentist.professionals.repositories.SpecialtyRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecialtyServiceImpl implements SpecialtyService {

    private final SpecialtyRepository repository;

    public SpecialtyServiceImpl(SpecialtyRepository repository) {
        this.repository = repository;
    }

    @Override
    public SpecialtyResponseDTO addSpecialty(SpecialtyRequestDTO dto) {
        if (repository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("La especialidad ya existe.");
        }

        Specialty specialty = new Specialty();
        specialty.setName(dto.getName());
        repository.save(specialty);

        return new SpecialtyResponseDTO(specialty.getId(), specialty.getName());
    }

    @Override
    public SpecialtyResponseDTO updateSpecialty(Long id, SpecialtyRequestDTO dto) {
        Specialty specialty = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada"));

        specialty.setName(dto.getName());
        repository.save(specialty);

        return new SpecialtyResponseDTO(specialty.getId(), specialty.getName());
    }

    @Override
    public List<SpecialtyResponseDTO> getAllSpecialties() {
        return repository.findAll().stream()
                .map(s -> new SpecialtyResponseDTO(s.getId(), s.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSpecialty(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Especialidad no encontrada");
        }
        repository.deleteById(id);
    }
}

