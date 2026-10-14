package com.martinez.dentist.patients.repositories;

import com.martinez.dentist.patients.models.ClinicalFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ClinicalFileRepository extends JpaRepository<ClinicalFile, Long> {

    // Datos de los archivos sin cargar su contenido: el tamaño lo calcula la base.
    // Cada fila: id, file_name, file_type, tamaño en bytes, clinical_history_id
    @Query(value = "SELECT id, file_name, file_type, OCTET_LENGTH(data), clinical_history_id " +
            "FROM clinical_files WHERE clinical_history_id IN (:historyIds)", nativeQuery = true)
    List<Object[]> findFileSummariesByHistoryIds(@Param("historyIds") Collection<Long> historyIds);
}
