package org.example.ecommerce.service;

import org.example.ecommerce.exception.ECommerceException;
import org.example.ecommerce.model.Cart;
import org.example.ecommerce.model.Product;
import org.example.ecommerce.model.User;
import org.example.ecommerce.repository.CartRepository;
import org.example.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    public Cart getCart(Long userId) {
        if (userId == null) {
            throw new ECommerceException("User ID must not be null");
        }
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            // Initialize cart for user if it doesn't exist
            User user = userRepository.getUserById(userId)
                    .orElseThrow(() -> new ECommerceException("User not found with id: " + userId));
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart);
        }
        return cart;
    }

    public void addToCart(Long userId, Product product, int quantity) {
        Cart cart = getCart(userId);
        cart.addItem(product, quantity);
        cartRepository.save(cart);
    }

    public void updateQuantity(Long userId, Long productId, int quantity) {
        Cart cart = getCart(userId);
        cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    if (quantity <= 0) {
                        cart.removeItem(productId);
                    } else {
                        item.setQuantity(quantity);
                    }
                });
        cartRepository.save(cart);
    }

    public void removeFromCart(Long userId, Long productId) {
        Cart cart = getCart(userId);
        cart.removeItem(productId);
        cartRepository.save(cart);
    }

    public void clearCart(Long userId) {
        Cart cart = getCart(userId);
        cart.clear();
        cartRepository.save(cart);
    }
}
