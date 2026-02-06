package org.example.ecommerce.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.example.ecommerce.model.User;
import org.example.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryUserRepository implements UserRepository {
	private final List<User> users = new ArrayList<>();
	private final AtomicLong counter = new AtomicLong();

	public InMemoryUserRepository() {
		save(new User(null, "admin", "admin@example.com", "password", "ROLE_ADMIN", null, null, null));
		save(new User(null, "user1", "user1@example.com", "password", "ROLE_USER", null, null, null));
	}

	@Override
	public List<User> findAll() {
		return new ArrayList<>(users);
	}

	@Override
	public Optional<User> getUserById(Long id) {
		return Optional.ofNullable(users.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null));
	}

	@Override
	public User save(User user) {
		if (user.getId() == null) {
			user.setId(counter.incrementAndGet());
			users.add(user);
		} else {
			Optional<User> existing = getUserById(user.getId());
			if (existing.isPresent()) {
				User u = existing.get();
				u.setUsername(user.getUsername());
				u.setEmail(user.getEmail());
				u.setRole(user.getRole());
				u.setProfilePicture(user.getProfilePicture());
				u.setBirthDate(user.getBirthDate());
			}
		}
		return user;
	}

	@Override
	public void deleteById(Long id) {
		users.removeIf(u -> u.getId().equals(id));
	}

	@Override
	public User findByEmail(String email) {
		return users.stream().filter(u -> u.getEmail().equals(email)).findAny().orElse(null);
	}

	@Override
	public Optional<User> findByUsername(String username) {
		return users.stream().filter(u -> u.getUsername().equals(username)).findFirst();
	}

}
