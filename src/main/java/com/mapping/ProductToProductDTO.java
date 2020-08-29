package com.mapping;

import com.model.dto.ProductDTO;
import com.model.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductToProductDTO {
    ProductToProductDTO instance = Mappers.getMapper(ProductToProductDTO.class);

    ProductDTO convert(Product product);
}
