package org.example.ecommerce.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.example.ecommerce.model.User;
import org.example.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final List<User> users = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    public InMemoryUserRepository() {
        save(new User(null, "admin", "admin@example.com", "ADMIN", null, null));
        save(new User(null, "user1", "user1@example.com", "USER", null, null));
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
                existing.setProfilePicture(user.getProfilePicture());
                existing.setBirthDate(user.getBirthDate());
            }
        }
        return user;
    }

    @Override
    public void delete(Long id) {
        users.removeIf(u -> u.getId().equals(id));
    }

    @Override
    public User findByEmail(String email) {
        // TODO Auto-generated method stub
        return users.stream().filter(u -> u.getEmail().equals(email)).findAny().orElse(null);
    }

}
