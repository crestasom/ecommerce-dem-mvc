package org.example.ecommerce.controller;

import org.example.ecommerce.model.Cart;
import org.example.ecommerce.model.User;
import org.example.ecommerce.repository.CartRepository;
import org.example.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to demonstrate EAGER vs LAZY fetch types in JPA.
 * 
 * EAGER fetch: Related entities are loaded immediately with the parent entity.
 * LAZY fetch: Related entities are loaded only when accessed (on-demand).
 * 
 * Check the console logs to see SQL queries and when data is loaded.
 */
@RestController
@RequestMapping("/api/fetch-demo")
public class FetchTypeExamplesController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartRepository cartRepository;

	/**
	 * Demonstrates EAGER fetch behavior. Cart -> User relationship is EAGER, so
	 * user data is loaded immediately. Check console: You'll see JOIN query
	 * fetching both cart and user data.
	 */
	@GetMapping("/eager/{cartId}")
	public String demonstrateEagerFetch(@PathVariable Long cartId) {
		System.out.println("=== EAGER FETCH DEMONSTRATION ===");
		System.out.println("Fetching cart with ID: " + cartId);

		Cart cart = cartRepository.getCartById(cartId).orElse(null);
		if (cart == null) {
			return "Cart not found";
		}

		System.out.println("Cart loaded. Now accessing user...");
		// User is already loaded due to EAGER fetch
		User user = cart.getUser();

		return "EAGER Fetch: Cart ID=" + cart.getId() + ", User=" + (user != null ? user.getUsername() : "null")
				+ ". Check console logs - user was loaded immediately with cart.";
	}

	/**
	 * Demonstrates LAZY fetch behavior. Cart -> CartItems relationship is LAZY, so
	 * items are loaded on-demand. Check console: You'll see separate query when
	 * items are accessed.
	 */
	@GetMapping("/lazy/{cartId}")
	public String demonstrateLazyFetch(@PathVariable Long cartId) {
		System.out.println("=== LAZY FETCH DEMONSTRATION ===");
		System.out.println("Fetching cart with ID: " + cartId);

		Cart cart = cartRepository.getCartById(cartId).orElse(null);
		if (cart == null) {
			return "Cart not found";
		}

		System.out.println("Cart loaded. Items NOT loaded yet (LAZY).");
		System.out.println("Now accessing items...");

		// This triggers lazy loading of items
		int itemCount = cart.getItems().size();

		return "LAZY Fetch: Cart ID=" + cart.getId() + ", Item count=" + itemCount
				+ ". Check console logs - items were loaded only when accessed.";
	}

	/**
	 * Demonstrates the difference between EAGER and LAZY loading for collections.
	 * User -> Cart relationship is configured with cascade and orphan removal.
	 */
	@GetMapping("/user-cart/{userId}")
	public String demonstrateUserCart(@PathVariable Long userId) {
		System.out.println("=== USER CART FETCH DEMONSTRATION ===");
		System.out.println("Fetching user with ID: " + userId);

		User user = userRepository.getUserById(userId).orElse(null);
		if (user == null) {
			return "User not found";
		}

		System.out.println("User loaded. Now accessing cart...");
		Cart cart = user.getCart();

		return "User: " + user.getUsername() + ", Has cart: " + (cart != null)
				+ ". Check console logs to see fetch behavior.";
	}
}
