package org.example.ecommerce.model.dto;

import org.hibernate.validator.constraints.Length;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
	private Long id;

	@NotBlank(message = "Product name cannot be empty")
	@Length(min = 3, message = "Product name must be at least 3 characters long")
	private String name;

	@NotBlank(message = "Product description cannot be empty")
	private String description;

	@NotNull(message = "Price is required")
	@Positive(message = "Price must be greater than zero")
	private Double price;
}
