package org.example.ecommerce.service;

import org.example.ecommerce.model.Cart;
import org.example.ecommerce.model.Product;
import org.example.ecommerce.repository.CartRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {
	private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart getCart(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    public void addToCart(Long userId, Product product, int quantity) {
        Cart cart = getCart(userId);
        if (cart != null) {
            cart.addItem(product, quantity);
        }
    }

    public void removeFromCart(Long userId, Long productId) {
        Cart cart = getCart(userId);
        if (cart != null) {
            cart.removeItem(productId);
        }
    }

    public void clearCart(Long userId) {
        Cart cart = getCart(userId);
        if (cart != null) {
            cart.clear();
        }
    }
}
