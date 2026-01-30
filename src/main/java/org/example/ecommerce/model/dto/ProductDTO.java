package org.example.ecommerce.model.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDTO {
	private Long id;
	@NotNull
	@Length(min = 3)
	private String name;
	@NotNull
	private String description;
	@Min(value = 1l)
	private Double price;
}
