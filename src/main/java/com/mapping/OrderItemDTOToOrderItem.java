package com.mapping;

import com.model.dto.OrderItemDTO;
import com.model.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderItemDTOToOrderItem {
    OrderItemDTOToOrderItem instance = Mappers.getMapper(OrderItemDTOToOrderItem.class);

    OrderItem convert(OrderItemDTO orderItem);
}
