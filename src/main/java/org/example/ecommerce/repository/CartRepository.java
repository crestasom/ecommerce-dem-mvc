package org.example.ecommerce.repository;

import org.example.ecommerce.model.Cart;

public interface CartRepository {
    Cart findByUserId(Long userId);
}
