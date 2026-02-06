package org.example.ecommerce.service;

import java.util.Collections;
import java.util.Optional;

import org.example.ecommerce.model.User;
import org.example.ecommerce.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> dbUser = userRepository.findByUsername(username);

		return dbUser
				.map(user -> new org.springframework.security.core.userdetails.User(user.getUsername(),
						user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))))
				.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
	}
}
