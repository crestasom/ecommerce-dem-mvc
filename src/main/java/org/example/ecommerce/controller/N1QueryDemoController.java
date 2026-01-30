package org.example.ecommerce.controller;

import org.example.ecommerce.model.Cart;
import org.example.ecommerce.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to demonstrate the N+1 query problem and its solutions.
 * 
 * N+1 Problem: When fetching a collection of entities, if related entities are
 * loaded lazily, it results in 1 query for the main entities + N queries for
 * each related entity.
 * 
 * In this demo, we show how accessing cart items can trigger additional
 * queries.
 */
@RestController
@RequestMapping("/api/n1-demo")
public class N1QueryDemoController {

	@Autowired
	private CartRepository cartRepository;

	/**
	 * Demonstrates the N+1 query problem with cart items.
	 * This will execute:
	 * - 1 query to fetch the cart
	 * - Additional query to fetch items when accessed (if LAZY)
	 * 
	 * Check console logs to see SQL queries.
	 */
	@GetMapping("/problem/{userId}")
	public String demonstrateN1Problem(@PathVariable Long userId) {
		System.out.println("=== N+1 QUERY PROBLEM DEMONSTRATION ===");
		System.out.println("Fetching cart for user ID: " + userId);

		Cart cart = cartRepository.findByUserId(userId);

		if (cart == null) {
			return "Cart not found for user " + userId;
		}

		System.out.println("Cart loaded. Now accessing items...");

		// This triggers additional query for items (if LAZY)
		int totalItems = cart.getItems().size();

		return "Cart ID: " + cart.getId() + " with " + totalItems
				+ " items. Check console for SQL queries.";
	}

	/**
	 * Demonstrates EAGER loading of cart items.
	 * With proper fetch configuration, items are loaded immediately.
	 */
	@GetMapping("/eager-fetch/{cartId}")
	public String demonstrateEagerFetch(@PathVariable Long cartId) {
		System.out.println("=== EAGER FETCH DEMONSTRATION ===");
		System.out.println("Fetching cart with ID: " + cartId);

		Cart cart = cartRepository.getCartById(cartId).orElse(null);

		if (cart == null) {
			return "Cart not found";
		}

		System.out.println("Cart loaded. Accessing items...");
		int totalItems = cart.getItems().size();

		return "Cart ID: " + cart.getId() + " with " + totalItems
				+ " items. Check console - items may be loaded with cart or separately.";
	}

	/**
	 * Performance comparison endpoint.
	 * Shows the difference between lazy and eager loading.
	 */
	@GetMapping("/compare/{userId}")
	public String comparePerformance(@PathVariable Long userId) {
		System.out.println("\n========================================");
		System.out.println("PERFORMANCE COMPARISON");
		System.out.println("========================================\n");

		System.out.println("--- Fetching cart and accessing items ---");
		Cart cart = cartRepository.findByUserId(userId);

		if (cart == null) {
			return "Cart not found for user " + userId;
		}

		int itemCount = cart.getItems().size();

		System.out.println("\n========================================");
		System.out.println("Check the console logs above to see SQL queries!");
		System.out.println("========================================\n");

		return "Performance comparison complete. Cart has " + itemCount +
				" items. Check console logs to see query patterns.";
	}
}
