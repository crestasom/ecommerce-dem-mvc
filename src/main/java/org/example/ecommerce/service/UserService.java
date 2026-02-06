package org.example.ecommerce.service;

import java.util.List;

import org.example.ecommerce.exception.ECommerceException;
import org.example.ecommerce.model.User;
import org.example.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public User findById(Long id) {
		if (id == null) {
			throw new ECommerceException("User ID must not be null");
		}
		return userRepository.getUserById(id)
				.orElseThrow(() -> new ECommerceException("User not found for id " + id));
	}

	public void save(User user) {
		userRepository.save(user);
	}

	public void delete(Long id) {
		if (id == null) {
			throw new ECommerceException("User ID must not be null");
		}
		userRepository.deleteById(id);
	}

	public boolean isUserEmailExists(String email) {
		User user = userRepository.findByEmail(email);
		return user == null ? false : true;
	}

	public User findByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new ECommerceException("User not found: " + username));
	}
}
