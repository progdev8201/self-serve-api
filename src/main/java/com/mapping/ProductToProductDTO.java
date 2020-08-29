package com.mapping;

import com.model.dto.ProductDTO;
import com.model.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductToProductDTO {
    ProductToProductDTO instance = Mappers.getMapper(ProductToProductDTO.class);

    @Mapping(target = "menu", ignore = true)
    @Mapping(target = "options", ignore = true)
    @Mapping(target = "rates", ignore = true)
    ProductDTO convert(Product product);
}
