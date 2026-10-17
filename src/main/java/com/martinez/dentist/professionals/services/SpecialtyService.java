package com.martinez.dentist.professionals.services;

import com.martinez.dentist.professionals.controllers.specialty.SpecialtyRequestDTO;
import com.martinez.dentist.professionals.controllers.specialty.SpecialtyResponseDTO;
import com.martinez.dentist.professionals.models.ProfessionalSpecialty;
import com.martinez.dentist.professionals.models.Specialty;
import com.martinez.dentist.professionals.repositories.ProfessionalSpecialtyRepository;
import com.martinez.dentist.professionals.repositories.SpecialtyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecialtyService {

    static final int MAX_NAME_LENGTH = 100;

    private final SpecialtyRepository repository;
    private final ProfessionalSpecialtyRepository professionalSpecialtyRepository;

    public SpecialtyService(SpecialtyRepository repository,
                            ProfessionalSpecialtyRepository professionalSpecialtyRepository) {
        this.repository = repository;
        this.professionalSpecialtyRepository = professionalSpecialtyRepository;
    }

    public SpecialtyResponseDTO addSpecialty(SpecialtyRequestDTO dto) {
        String name = normalizeName(dto == null ? null : dto.getName());
        if (repository.existsByNameIgnoreCase(name)) {
            throw new IllegalArgumentException("Ya existe la especialidad \"" + name + "\".");
        }

        Specialty specialty = new Specialty();
        specialty.setName(name);
        repository.save(specialty);

        return new SpecialtyResponseDTO(specialty.getId(), specialty.getName());
    }

    public SpecialtyResponseDTO updateSpecialty(Long id, SpecialtyRequestDTO dto) {
        Specialty specialty = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada"));

        String name = normalizeName(dto == null ? null : dto.getName());
        if (repository.existsByNameIgnoreCaseAndIdNot(name, id)) {
            throw new IllegalArgumentException("Ya existe la especialidad \"" + name + "\".");
        }

        specialty.setName(name);
        repository.save(specialty);

        return new SpecialtyResponseDTO(specialty.getId(), specialty.getName());
    }

    public List<SpecialtyResponseDTO> getAllSpecialties() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(Specialty::getName, String.CASE_INSENSITIVE_ORDER))
                .map(s -> new SpecialtyResponseDTO(s.getId(), s.getName()))
                .collect(Collectors.toList());
    }

    // No se borra una especialidad que tiene profesionales: el borrado en cascada se la sacaría
    // de su ficha sin avisar y podría dejar a un profesional sin ninguna especialidad.
    @Transactional
    public void deleteSpecialty(Long id) {
        Specialty specialty = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada"));

        List<String> professionals = professionalSpecialtyRepository.findBySpecialtyId(id).stream()
                .map(ProfessionalSpecialty::getProfessional)
                .map(p -> p.getFullName())
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());

        if (!professionals.isEmpty()) {
            String who = professionals.size() == 1
                    ? "la tiene asignada " + professionals.get(0)
                    : "la tienen asignada " + professionals.size() + " profesionales (" + String.join(", ", professionals) + ")";
            throw new IllegalArgumentException("No se puede eliminar \"" + specialty.getName() + "\": " + who
                    + ". Quitala de su ficha primero.");
        }

        repository.delete(specialty);
    }

    // Saca espacios de más y valida que no quede vacío ni pase el largo de la columna
    static String normalizeName(String raw) {
        String name = raw == null ? "" : raw.trim().replaceAll("\\s+", " ");
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Escribí el nombre de la especialidad.");
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("El nombre de la especialidad puede tener hasta " + MAX_NAME_LENGTH + " caracteres.");
        }
        return name;
    }
}
