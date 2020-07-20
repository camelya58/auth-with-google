package com.github.camelya58.auth_with_google.repository;

import com.github.camelya58.auth_with_google.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface UserRepository is responsible for connecting to PostgreSQL.
 *
 * @author Kamila Meshcheryakova
 * created 17.07.2020
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String email);

    User findByName(String name);

    User findByGoogleName(String googleName);

    User findByGoogleUsername(String googleUsername);
}
