package org.example.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.example.ecommerce.model.User;

public interface UserRepository {
    List<User> findAll();

    Optional<User> getUserById(Long id);

    User save(User user);

    void deleteById(Long id);

    User findByEmail(String email);

}
