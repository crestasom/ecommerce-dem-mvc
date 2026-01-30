package org.example.ecommerce.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "carts")
public class Cart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// --- HIBERNATE MAPPING EXAMPLES ---

	/**
	 * One-to-One relationship with User.
	 * 
	 * @JoinColumn(name = "user_id"): Defines the foreign key column in the 'carts'
	 *                  table.
	 *                  unique = true: Ensures that each user can have only one
	 *                  cart.
	 * 
	 *                  fetch = FetchType.EAGER: The associated User will be fetched
	 *                  immediately when
	 *                  the Cart is loaded. This can lead to N+1 problems if not
	 *                  used carefully.
	 */
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", unique = true)
	private User user;

	/**
	 * One-to-Many relationship with CartItem.
	 * 
	 * mappedBy = "cart": Indicates that the 'cart' field in the CartItem entity
	 * owns the relationship.
	 * 
	 * cascade = CascadeType.ALL: All operations on Cart will be cascaded to
	 * CartItems.
	 * 
	 * orphanRemoval = true: Removing a CartItem from the 'items' list will
	 * also delete it from the database.
	 * 
	 * fetch = FetchType.LAZY: CartItems are loaded only when accessed.
	 * This is the recommended default for collections.
	 */
	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<CartItem> items = new ArrayList<>();

	public Double getTotal() {
		return items.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
	}

	public void addItem(Product product, int quantity) {
		for (CartItem item : items) {
			if (item.getProduct().getId().equals(product.getId())) {
				item.setQuantity(item.getQuantity() + quantity);
				return;
			}
		}
		CartItem newItem = new CartItem();
		newItem.setProduct(product);
		newItem.setQuantity(quantity);
		newItem.setPrice(product.getPrice());
		newItem.setCart(this);
		items.add(newItem);
	}

	public void removeItem(Long productId) {
		items.removeIf(item -> item.getProduct().getId().equals(productId));
	}

	public void clear() {
		items.clear();
	}
}
