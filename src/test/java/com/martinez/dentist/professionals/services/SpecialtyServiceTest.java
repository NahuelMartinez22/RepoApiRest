package com.martinez.dentist.professionals.services;

import com.martinez.dentist.professionals.controllers.specialty.SpecialtyRequestDTO;
import com.martinez.dentist.professionals.controllers.specialty.SpecialtyResponseDTO;
import com.martinez.dentist.professionals.models.Professional;
import com.martinez.dentist.professionals.models.ProfessionalSpecialty;
import com.martinez.dentist.professionals.models.Specialty;
import com.martinez.dentist.professionals.repositories.ProfessionalSpecialtyRepository;
import com.martinez.dentist.professionals.repositories.SpecialtyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SpecialtyServiceTest {

    private SpecialtyRepository repository;
    private ProfessionalSpecialtyRepository professionalSpecialtyRepository;
    private SpecialtyService service;

    @BeforeEach
    void setUp() {
        repository = mock(SpecialtyRepository.class);
        professionalSpecialtyRepository = mock(ProfessionalSpecialtyRepository.class);
        service = new SpecialtyService(repository, professionalSpecialtyRepository);
    }

    @Test
    void creaConElNombreLimpio() {
        when(repository.existsByNameIgnoreCase("Ortodoncia infantil")).thenReturn(false);

        SpecialtyResponseDTO creada = service.addSpecialty(dto("  Ortodoncia   infantil "));

        assertThat(creada.getName()).isEqualTo("Ortodoncia infantil");
        verify(repository).save(any(Specialty.class));
    }

    @Test
    void noCreaUnNombreVacioNiRepetido() {
        assertThatThrownBy(() -> service.addSpecialty(dto("   ")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Escribí el nombre");

        when(repository.existsByNameIgnoreCase("Ortodoncia")).thenReturn(true);
        assertThatThrownBy(() -> service.addSpecialty(dto("Ortodoncia")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ya existe");

        verify(repository, never()).save(any());
    }

    @Test
    void alEditarNoPermiteElNombreDeOtraEspecialidad() {
        when(repository.findById(2L)).thenReturn(Optional.of(specialty(2L, "Endodoncia")));
        when(repository.existsByNameIgnoreCaseAndIdNot("Ortodoncia", 2L)).thenReturn(true);

        assertThatThrownBy(() -> service.updateSpecialty(2L, dto("Ortodoncia")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ya existe");
    }

    @Test
    void alEditarAceptaCambiarSoloMayusculas() {
        Specialty endo = specialty(2L, "endodoncia");
        when(repository.findById(2L)).thenReturn(Optional.of(endo));
        when(repository.existsByNameIgnoreCaseAndIdNot("Endodoncia", 2L)).thenReturn(false);

        assertThat(service.updateSpecialty(2L, dto("Endodoncia")).getName()).isEqualTo("Endodoncia");
    }

    @Test
    void noBorraUnaEspecialidadAsignada() {
        Specialty orto = specialty(1L, "Ortodoncia");
        when(repository.findById(1L)).thenReturn(Optional.of(orto));
        when(professionalSpecialtyRepository.findBySpecialtyId(1L))
                .thenReturn(List.of(assignment("Dr. Martín Sosa", orto)));

        assertThatThrownBy(() -> service.deleteSpecialty(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Dr. Martín Sosa");

        verify(repository, never()).delete(any());
    }

    @Test
    void borraUnaEspecialidadSinProfesionales() {
        Specialty perio = specialty(4L, "Periodoncia");
        when(repository.findById(4L)).thenReturn(Optional.of(perio));
        when(professionalSpecialtyRepository.findBySpecialtyId(4L)).thenReturn(List.of());

        service.deleteSpecialty(4L);

        verify(repository).delete(perio);
    }

    private static SpecialtyRequestDTO dto(String name) {
        SpecialtyRequestDTO dto = new SpecialtyRequestDTO();
        dto.setName(name);
        return dto;
    }

    private static Specialty specialty(Long id, String name) {
        Specialty s = new Specialty();
        s.setId(id);
        s.setName(name);
        return s;
    }

    private static ProfessionalSpecialty assignment(String professionalName, Specialty specialty) {
        Professional p = mock(Professional.class);
        when(p.getFullName()).thenReturn(professionalName);
        ProfessionalSpecialty ps = new ProfessionalSpecialty();
        ps.setProfessional(p);
        ps.setSpecialty(specialty);
        return ps;
    }
}
