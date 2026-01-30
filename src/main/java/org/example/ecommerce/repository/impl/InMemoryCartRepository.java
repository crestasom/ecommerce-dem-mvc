package org.example.ecommerce.repository.impl;

import org.example.ecommerce.model.Cart;
import org.example.ecommerce.repository.CartRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

import java.util.Optional;

@Repository
public class InMemoryCartRepository implements CartRepository {
    private final Map<Long, Cart> userCarts = new HashMap<>();
    private final Map<Long, Cart> idCarts = new HashMap<>();

    @Override
    public Cart findByUserId(Long userId) {
        if (userId == null)
            return null;
        return userCarts.computeIfAbsent(userId, k -> {
            Cart cart = new Cart();
            // In-memory assignment of ID for consistency
            cart.setId((long) (idCarts.size() + 1));
            idCarts.put(cart.getId(), cart);
            return cart;
        });
    }

    @Override
    public Cart save(Cart cart) {
        if (cart.getId() == null) {
            cart.setId((long) (idCarts.size() + 1));
        }
        idCarts.put(cart.getId(), cart);
        if (cart.getUser() != null) {
            userCarts.put(cart.getUser().getId(), cart);
        }
        return cart;
    }

    @Override
    public Optional<Cart> getCartById(Long id) {
        return Optional.ofNullable(idCarts.get(id));
    }
}
