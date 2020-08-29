package com.mapping;

import com.model.dto.OrderItemDTO;
import com.model.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderItemDTOToOrderItem {
    OrderItemDTOToOrderItem instance = Mappers.getMapper(OrderItemDTOToOrderItem.class);

    @Mapping(target = "product", ignore = true)
    OrderItem convert(OrderItemDTO orderItem);
}
