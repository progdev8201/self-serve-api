package com.mapping;

import com.model.dto.ProductDTO;
import com.model.entity.OrderItem;
import com.model.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductToOrderItems {
    ProductToOrderItems instance = Mappers.getMapper(ProductToOrderItems.class);

    @Mapping(target = "option", ignore = true)
    @Mapping(target = "id", ignore = true)
    OrderItem convert(Product product);
}
