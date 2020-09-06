package com.service;

import com.model.dto.OrderItemDTO;
import com.model.entity.OrderItem;
import com.model.enums.ProgressStatus;
import com.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KitchenService {
    @Autowired
    private OrderItemRepository orderItemRepository;

    public void changeOrderItemStatus(OrderItemDTO orderItemDTO){
        OrderItem orderItem = orderItemRepository.findById(orderItemDTO.getId()).get();
        orderItem.setOrderStatus(ProgressStatus.READY);
        orderItemRepository.save(orderItem);

    }
}
