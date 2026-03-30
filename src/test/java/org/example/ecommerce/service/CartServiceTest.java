package org.example.ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.example.ecommerce.model.Cart;
import org.example.ecommerce.model.CartItem;
import org.example.ecommerce.repository.CartRepository;
import org.example.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit Test for ProductService Focusing on Product Management Features: Find,
 * Save, Delete. These tests demonstrate Mockito-based isolation for the Service
 * Layer.
 */
@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

	@Mock
	private CartRepository cartRepository;
	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private CartService cartService;

	private Cart cart;
//	private ProductDTO productDTO;

	@BeforeEach
	void setUp() {
		cart = new Cart();
		CartItem item = new CartItem();
		item.setId(1l);
		item.setPrice(50.0);
		item.setQuantity(1);
		cart.getItems().add(item);

	}

	@Test
	@DisplayName("Test Find All Products - Success Scenario")
	void testGetCart() {
		// Arrange: Mock the repository to return a list of products
		when(cartRepository.findByUserId(anyLong())).thenReturn(cart);

		Cart c = cartService.getCart(1l);

		// Assert: Verify results and repository interaction
		assertNotNull(c);
		assertEquals(1, c.getItems().size());
		assertEquals(50.0, c.getItems().get(0).getPrice());
		verify(cartRepository, times(1)).findByUserId(anyLong());
	}

}
