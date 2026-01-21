package org.example.ecommerce.service;

import java.util.List;

import org.example.ecommerce.model.User;
import org.example.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public User findById(Long id) {
		return userRepository.findById(id);
	}

	public void save(User user) {
		userRepository.save(user);
	}

	public void delete(Long id) {
		userRepository.delete(id);
	}

	public boolean isUserEmailExists(String email) {
		User user = userRepository.findByEmail(email);
		return user == null ? false : true;
	}
}
