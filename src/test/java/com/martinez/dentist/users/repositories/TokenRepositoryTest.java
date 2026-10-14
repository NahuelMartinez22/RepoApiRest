package com.martinez.dentist.users.repositories;

import com.martinez.dentist.users.models.Token;
import com.martinez.dentist.users.models.User;
import com.martinez.dentist.users.models.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:tokens;MODE=MySQL;DATABASE_TO_LOWER=TRUE",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class TokenRepositoryTest {

    @Autowired
    private TokenRepository repository;

    @Autowired
    private TestEntityManager em;

    @Test
    void borraVencidosYLosAnterioresDelUsuarioPeroNoLosVigentesDeOtros() {
        User ana = em.persist(new User("ana", "x", "ana@clinica.com", UserRole.values()[0]));
        User beto = em.persist(new User("beto", "x", "beto@clinica.com", UserRole.values()[0]));
        LocalDateTime now = LocalDateTime.now();

        token(ana, "ana-vigente", now.plusMinutes(30));
        token(beto, "beto-vencido", now.minusMinutes(1));
        token(beto, "beto-vigente", now.plusMinutes(30));
        em.flush();

        int deleted = repository.deleteExpiredOrOwnedBy(now, ana);
        em.clear();

        assertThat(deleted).isEqualTo(2);
        assertThat(repository.findByToken("ana-vigente")).isEmpty();
        assertThat(repository.findByToken("beto-vencido")).isEmpty();
        assertThat(repository.findByToken("beto-vigente")).isPresent();
    }

    private void token(User user, String value, LocalDateTime expiration) {
        Token token = new Token();
        token.setUser(user);
        token.setToken(value);
        token.setExpirationDate(expiration);
        em.persist(token);
    }
}
