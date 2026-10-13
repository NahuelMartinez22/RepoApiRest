package com.martinez.dentist.patients.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;DATABASE_TO_LOWER=TRUE",
        "spring.jpa.hibernate.ddl-auto=none"
})
class ClinicalFileRepositoryTest {

    @Autowired
    private ClinicalFileRepository repository;

    @Autowired
    private JdbcTemplate jdbc;

    @Test
    void devuelveElTamanioSinCargarElContenido() {
        jdbc.execute("CREATE TABLE clinical_files (id BIGINT PRIMARY KEY, file_name VARCHAR(255), " +
                "file_type VARCHAR(255), data LONGBLOB NOT NULL, clinical_history_id BIGINT NOT NULL)");
        jdbc.update("INSERT INTO clinical_files VALUES (?, ?, ?, ?, ?)", 1L, "rx.png", "image/png", new byte[1500], 10L);
        jdbc.update("INSERT INTO clinical_files VALUES (?, ?, ?, ?, ?)", 2L, "informe.pdf", "application/pdf", new byte[42], 10L);
        jdbc.update("INSERT INTO clinical_files VALUES (?, ?, ?, ?, ?)", 3L, "otro.pdf", "application/pdf", new byte[7], 99L);

        List<Object[]> rows = repository.findFileSummariesByHistoryIds(List.of(10L));

        assertThat(rows).hasSize(2);
        assertThat(rows).anySatisfy(r -> {
            assertThat(((Number) r[0]).longValue()).isEqualTo(1L);
            assertThat(r[1]).isEqualTo("rx.png");
            assertThat(r[2]).isEqualTo("image/png");
            assertThat(((Number) r[3]).longValue()).isEqualTo(1500L);
            assertThat(((Number) r[4]).longValue()).isEqualTo(10L);
        });
        assertThat(rows).anySatisfy(r -> assertThat(((Number) r[3]).longValue()).isEqualTo(42L));
    }
}
