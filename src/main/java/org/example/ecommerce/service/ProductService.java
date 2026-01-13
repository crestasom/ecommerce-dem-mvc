package org.example.ecommerce.service;

import java.util.List;

import org.example.ecommerce.model.Product;
import org.example.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public List<Product> findAll() {
		return productRepository.findAll();
	}

	public Product findById(Long id) {
		return productRepository.findById(id);
	}

	public void save(Product product) {
		productRepository.save(product);
	}

	public void delete(Long id) {
		productRepository.delete(id);
	}
}
