package org.example.ecommerce.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.example.ecommerce.exception.ECommerceException;
import org.example.ecommerce.model.Product;
import org.example.ecommerce.model.dto.ProductDTO;
import org.example.ecommerce.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;
	// private final ProductMapper mapper;

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

	public List<ProductDTO> findAll() {
		return productRepository.findAll().stream().map(p -> toDTO(p)).collect(Collectors.toList());
	}

	public ProductDTO findById(Long id) {
		if (id == null) {
			throw new ECommerceException("Product ID must not be null");
		}
		Optional<Product> p = productRepository.getProductById(id);
		if (p.isEmpty()) {
			throw new ECommerceException("Product not found for id " + id);
		}
		return toDTO(p.get());
	}

	public Product findEntityById(Long id) {
		if (id == null) {
			throw new ECommerceException("Product ID must not be null");
		}
		return productRepository.getProductById(id)
				.orElseThrow(() -> new ECommerceException("Product not found for id " + id));
	}

	public void save(ProductDTO productDto) {
		try {
			Product product = toEntity(productDto);
			// Product product =
			// Product.builder().name(productDto.getName()).description(productDto.getDescription())
			// .price(productDto.getPrice()).build();
			productRepository.save(product);
			LOGGER.info("product has been stored [{}]", product);
		} catch (Exception ex) {
			LOGGER.error("exception occured [{}]", ex.getMessage(), ex);
		}
	}

	private ProductDTO toDTO(Product p) {
		return ProductDTO.builder().name(p.getName()).description(p.getDescription()).price(p.getPrice()).id(p.getId())
				.build();
	}

	private Product toEntity(ProductDTO p) {
		return Product.builder().name(p.getName()).description(p.getDescription()).price(p.getPrice()).id(p.getId())
				.build();
	}

	public void delete(Long id) {
		if (id == null) {
			throw new ECommerceException("Product ID must not be null");
		}
		productRepository.deleteById(id);
	}
}
