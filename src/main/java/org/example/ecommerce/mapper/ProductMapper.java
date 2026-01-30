package org.example.ecommerce.mapper;

import org.example.ecommerce.model.Product;
import org.example.ecommerce.model.dto.ProductDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

	Product toEntity(ProductDTO dto);

	ProductDTO toDTO(Product product);
}
