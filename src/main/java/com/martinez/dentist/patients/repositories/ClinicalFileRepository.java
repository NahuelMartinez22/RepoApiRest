package com.martinez.dentist.patients.repositories;

import com.martinez.dentist.patients.models.ClinicalFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicalFileRepository extends JpaRepository<ClinicalFile, Long> {}