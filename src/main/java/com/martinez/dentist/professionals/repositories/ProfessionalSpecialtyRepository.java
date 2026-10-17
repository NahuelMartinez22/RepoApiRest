package com.martinez.dentist.professionals.repositories;

import com.martinez.dentist.professionals.models.ProfessionalSpecialty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfessionalSpecialtyRepository extends JpaRepository<ProfessionalSpecialty, Long> {
    List<ProfessionalSpecialty> findByProfessionalId(Long professionalId);
    List<ProfessionalSpecialty> findBySpecialtyId(Long specialtyId);
    boolean existsByProfessionalIdAndSpecialtyId(Long professionalId, Long specialtyId);
    void deleteByProfessionalIdAndSpecialtyId(Long professionalId, Long specialtyId);
}

