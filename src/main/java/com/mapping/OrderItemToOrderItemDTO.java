package com.mapping;

import com.model.dto.OrderItemDTO;
import com.model.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderItemToOrderItemDTO {
    OrderItemToOrderItemDTO instance = Mappers.getMapper(OrderItemToOrderItemDTO.class);

    @Mapping(target = "product", ignore = true)
    OrderItemDTO convert(OrderItem orderItem);
}
