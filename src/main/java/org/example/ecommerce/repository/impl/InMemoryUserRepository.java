package org.example.ecommerce.repository.impl;

import org.example.ecommerce.model.User;
import org.example.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final List<User> users = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    public InMemoryUserRepository() {
        save(new User(null, "admin", "admin@example.com", "ADMIN"));
        save(new User(null, "user1", "user1@example.com", "USER"));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    @Override
    public User findById(Long id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(counter.incrementAndGet());
            users.add(user);
        } else {
            User existing = findById(user.getId());
            if (existing != null) {
                existing.setUsername(user.getUsername());
                existing.setEmail(user.getEmail());
                existing.setRole(user.getRole());
            }
        }
        return user;
    }

    @Override
    public void delete(Long id) {
        users.removeIf(u -> u.getId().equals(id));
    }
}
