package org.example.ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.example.ecommerce.exception.ECommerceException;
import org.example.ecommerce.model.Product;
import org.example.ecommerce.model.dto.ProductDTO;
import org.example.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit Test for ProductService
 * Focusing on Product Management Features: Find, Save, Delete.
 * These tests demonstrate Mockito-based isolation for the Service Layer.
 */
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .name("Test Product")
                .description("Test Description")
                .price(99.99)
                .build();

        productDTO = ProductDTO.builder()
                .id(1L)
                .name("Test Product")
                .description("Test Description")
                .price(99.99)
                .build();
    }

    @Test
    @DisplayName("Test Find All Products - Success Scenario")
    void testFindAll() {
        // Arrange: Mock the repository to return a list of products
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));

        // Act: Call the service method
        List<ProductDTO> result = productService.findAll();

        // Assert: Verify results and repository interaction
        assertNotNull(result);
		assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test Find Product By ID - Success Scenario")
    void testFindById_Success() {
        // Arrange: Mock repository to return the product
		when(productRepository.getProductById(any())).thenReturn(Optional.of(product));
        // Act
        ProductDTO result = productService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(productRepository, times(1)).getProductById(1L);
    }

	@Test
    @DisplayName("Test Find Product By ID - Not Found Scenario")
    void testFindById_NotFound() {
        // Arrange: Mock repository to return empty optional
        when(productRepository.getProductById(1L)).thenReturn(Optional.empty());

        // Act & Assert: Expect ECommerceException
        ECommerceException exception = assertThrows(ECommerceException.class, () -> {
            productService.findById(1L);
        });

        assertTrue(exception.getMessage().contains("Product not found"));
    }

	@Test
    @DisplayName("Test Save Product - Success Scenario")
    void testSave() {
        // Act
        productService.save(productDTO);

        // Assert: Verify repository save was called with any Product
        verify(productRepository, times(1)).save(any(Product.class));
    }

	@Test
    @DisplayName("Test Delete Product - Success Scenario")
    void testDelete() {
        // Act

        productService.delete(1L);

        // Assert: Verify repository deleteById was called
        verify(productRepository, times(1)).deleteById(1L);
    }
}
