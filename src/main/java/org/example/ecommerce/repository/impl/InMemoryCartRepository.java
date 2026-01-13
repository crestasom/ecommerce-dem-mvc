package org.example.ecommerce.repository.impl;

import org.example.ecommerce.model.Cart;
import org.example.ecommerce.repository.CartRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryCartRepository implements CartRepository {
    private final Map<Long, Cart> userCarts = new HashMap<>();

    @Override
    public Cart findByUserId(Long userId) {
        if (userId == null)
            return null;
        return userCarts.computeIfAbsent(userId, k -> new Cart());
    }
}
