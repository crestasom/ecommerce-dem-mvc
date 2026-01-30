package org.example.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.example.ecommerce.model.Product;

public interface ProductRepository {
    List<Product> findAll();

    Optional<Product> getProductById(Long id);

    Product save(Product product);

    void deleteById(Long id);
}
