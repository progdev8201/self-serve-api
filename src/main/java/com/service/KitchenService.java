package com.service;

import com.mapping.ProductToProductDTO;
import com.model.dto.OrderItemDTO;
import com.mapping.OrderItemToOrderItemDTO;
import com.model.entity.OrderItem;
import com.model.enums.ProgressStatus;
import com.repository.OrderItemRepository;
import com.service.DtoUtil.DTOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class KitchenService {
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    DTOUtils dtoUtils;

    public OrderItemDTO changeOrderItemStatus(OrderItemDTO orderItemDTO){
        OrderItem orderItem = orderItemRepository.findById(orderItemDTO.getId()).get();
        orderItem.setOrderStatus(ProgressStatus.READY);
        orderItem=orderItemRepository.save(orderItem);

        OrderItemDTO returnValue = OrderItemToOrderItemDTO.instance.convert(orderItem);
        returnValue.setProduct(dtoUtils.generateProductDTO(orderItem.getProduct()));
        return returnValue;


    }
}
