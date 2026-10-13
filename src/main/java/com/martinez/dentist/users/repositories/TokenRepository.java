package com.martinez.dentist.users.repositories;

import com.martinez.dentist.users.models.Token;
import com.martinez.dentist.users.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);

    // Borra los tokens vencidos de todos los usuarios y los anteriores del usuario indicado,
    // así solo queda válido el último enlace enviado.
    @Modifying
    @Transactional
    @Query("DELETE FROM Token t WHERE t.expirationDate < :now OR t.user = :user")
    int deleteExpiredOrOwnedBy(@Param("now") LocalDateTime now, @Param("user") User user);
}
