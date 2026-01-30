package org.example.ecommerce.repository;

import java.util.Optional;
import org.example.ecommerce.model.Cart;

public interface CartRepository {
    Cart findByUserId(Long userId);

    Cart save(Cart cart);

    Optional<Cart> getCartById(Long id);
}
