package org.example.ecommerce.repository;

import java.util.List;

import org.example.ecommerce.model.User;

public interface UserRepository {
    List<User> findAll();

    User findById(Long id);

    User save(User user);

    void delete(Long id);

	User findByEmail(String email);

}
