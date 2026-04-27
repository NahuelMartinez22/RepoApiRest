package com.martinez.dentist.practices.repositories;

import com.martinez.dentist.practices.models.Practice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PracticeRepository extends JpaRepository<Practice, Long> {
}
