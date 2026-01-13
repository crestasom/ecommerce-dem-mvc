package org.example.ecommerce.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.example.ecommerce.model.Product;
import org.example.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryProductRepository implements ProductRepository {
    private final List<Product> products = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    public InMemoryProductRepository() {
        save(new Product(null, "Laptop", "High-performance laptop", 1200.0));
        save(new Product(null, "Smartphone", "Latest model smartphone", 800.0));
        save(new Product(null, "Headphones", "Noise-cancelling headphones", 200.0));
    }

    @Override
    public List<Product> findAll() {
		return products;
    }

    @Override
    public Product findById(Long id) {
        return products.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public Product save(Product product) {
        if (product.getId() == null) {
            product.setId(counter.incrementAndGet());
            products.add(product);
        } else {
            Product existing = findById(product.getId());
            if (existing != null) {
                existing.setName(product.getName());
                existing.setDescription(product.getDescription());
                existing.setPrice(product.getPrice());
            }
        }
        return product;
    }

    @Override
    public void delete(Long id) {
        products.removeIf(p -> p.getId().equals(id));
    }

	public void test() {
		System.out.println("this is specific method for InMemoryProductRepository");
	}
}
