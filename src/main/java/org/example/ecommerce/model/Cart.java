package org.example.ecommerce.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Cart {
	private List<CartItem> items = new ArrayList<>();



	public Double getTotal() {
		return items.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
	}

	public void addItem(Product product, int quantity) {
		for (CartItem item : items) {
			if (item.getProductId().equals(product.getId())) {
				item.setQuantity(item.getQuantity() + quantity);
				return;
			}
		}
		items.add(new CartItem(product.getId(), product.getName(), quantity, product.getPrice()));
	}

	public void removeItem(Long productId) {
		items.removeIf(item -> item.getProductId().equals(productId));
	}

	public void clear() {
		items.clear();
	}
}
