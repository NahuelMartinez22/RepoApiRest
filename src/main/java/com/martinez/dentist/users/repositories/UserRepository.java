package com.martinez.dentist.users.repositories;

import com.martinez.dentist.users.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@EnableJpaRepositories
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String Username);
    User findByEmail(String email);
}
