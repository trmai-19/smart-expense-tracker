package com.smartexpense.api.domain.repository;

import com.smartexpense.api.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    User save(User user);
}
