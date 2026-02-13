package org.example.ecommerce.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;

import org.example.ecommerce.model.dto.ProductDTO;
import org.example.ecommerce.service.JwtService;
import org.example.ecommerce.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Unit Test for ProductController Uses MockMvc to simulate HTTP requests and
 * verify Controller logic. Security is mocked to simulate ADMIN access.
 */
@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc
public class ProductControllerTest {

	@TestConfiguration
	@EnableMethodSecurity
	static class TestConfig {
		@Bean
		PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}

		@Bean
		JwtService jwtService() {
			return new JwtService();
		}
	}

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	private ProductDTO productDTO;

	@BeforeEach
	void setUp() {
		productDTO = ProductDTO.builder().id(1L).name("Demo Product").description("Demo Description").price(150.00)
				.build();
	}

	@Test
	@WithMockUser(roles = "USER")
	@DisplayName("Test List Products - Authorized for USER Role")
	void testListProducts() throws Exception {
		// Arrange
		when(productService.findAll()).thenReturn(Arrays.asList(productDTO));

		// Act & Assert
		mockMvc.perform(get("/products")).andExpect(status().isOk()).andExpect(view().name("products/list"))
				.andExpect(model().attributeExists("products"));

		verify(productService, times(1)).findAll();
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("Test Show Add Form - Authorized for ADMIN")
	void testShowAddForm_Admin() throws Exception {
		mockMvc.perform(get("/products/add")).andExpect(status().isOk()).andExpect(view().name("products/form"))
				.andExpect(model().attributeExists("product"));
	}

	@Test
	@WithMockUser(roles = "USER")
	@DisplayName("Test Show Add Form - Forbidden for regular USER")
	void testShowAddForm_User_Forbidden() throws Exception {
		mockMvc.perform(get("/products/add")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("Test Save Product - Success Scenario")
	void testSaveProduct() throws Exception {
		// Act & Assert: Include CSRF token as it's required by Spring Security
		mockMvc.perform(post("/products/save").with(csrf()).flashAttr("product", productDTO))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/products"))
				.andExpect(flash().attribute("message", "Product saved successfully!"));

		verify(productService, times(1)).save(any(ProductDTO.class));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("Test Delete Product - Success Scenario")
	void testDeleteProduct() throws Exception {
		mockMvc.perform(get("/products/delete/1")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/products"))
				.andExpect(flash().attribute("message", "Product deleted successfully!"));

		verify(productService, times(1)).delete(1L);
	}
}
