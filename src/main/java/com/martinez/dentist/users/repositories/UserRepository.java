package com.martinez.dentist.users.repositories;

import com.martinez.dentist.professionals.models.Professional;
import com.martinez.dentist.users.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByProfessionalId(Long professionalId);

    Optional<User> findByEmail(String email);

}
