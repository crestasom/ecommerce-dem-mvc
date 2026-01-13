package org.example.ecommerce.repository;

import org.example.ecommerce.model.Product;
import java.util.List;

public interface ProductRepository {
    List<Product> findAll();

    Product findById(Long id);

    Product save(Product product);

    void delete(Long id);
}
