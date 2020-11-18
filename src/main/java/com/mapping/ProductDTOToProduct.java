package com.mapping;

import com.model.dto.ProductDTO;
import com.model.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductDTOToProduct {
    ProductDTOToProduct instance = Mappers.getMapper(ProductDTOToProduct.class);

    @Mapping(target = "menu", ignore = true)
    @Mapping(target = "options", ignore = true)
    @Mapping(target = "rates", ignore = true)
    @Mapping(target = "checkItems", ignore = true)
    Product convert(ProductDTO productDTO);
}
