package org.example.ecommerce.repository.jpa;

import java.util.List;

import org.example.ecommerce.model.Product;
import org.example.ecommerce.repository.ProductRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Primary
public interface ProductJpaRepository extends JpaRepository<Product, Long>, ProductRepository {
    List<Product> findByNameContaining(String name);

    @Override
    default Optional<Product> getProductById(Long id) {
        return findById(id);
    }
}
