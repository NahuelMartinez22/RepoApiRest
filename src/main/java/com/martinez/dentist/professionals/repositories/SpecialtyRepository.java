package com.martinez.dentist.professionals.repositories;

import com.martinez.dentist.professionals.models.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {
    boolean existsByName(String name);
}
