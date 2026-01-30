package org.example.ecommerce.repository.jpa;

import org.example.ecommerce.model.User;
import org.example.ecommerce.repository.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Primary
public interface UserJpaRepository extends JpaRepository<User, Long>, UserRepository {
	@Override
	User findByEmail(String email);

	@Override
	default Optional<User> getUserById(Long id) {
		return findById(id);
	}

}
