package org.example.ecommerce.repository.jpa;

import java.util.Optional;

import org.example.ecommerce.model.Cart;
import org.example.ecommerce.repository.CartRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public interface CartJpaRepository extends JpaRepository<Cart, Long>, CartRepository {
	@Override
	@Query("SELECT c FROM Cart c LEFT JOIN FETCH c.items WHERE c.user.id = :userId")
	Cart findByUserId(Long userId);

	@Override
	default Optional<Cart> getCartById(Long id) {
		return findById(id);
	}

}
