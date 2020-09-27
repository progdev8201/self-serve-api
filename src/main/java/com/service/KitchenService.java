package com.service;

import com.mapping.ProductToProductDTO;
import com.model.dto.OrderItemDTO;
import com.mapping.OrderItemToOrderItemDTO;
import com.model.entity.Bill;
import com.model.entity.OrderItem;
import com.model.entity.Restaurant;
import com.model.enums.BillStatus;
import com.model.enums.OrderStatus;
import com.model.enums.ProductType;
import com.model.enums.ProgressStatus;
import com.repository.OrderItemRepository;
import com.repository.RestaurantRepository;
import com.service.DtoUtil.DTOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class KitchenService {
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;
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

    public List<OrderItemDTO> fetchWaiterRequest(Long restaurantId){
        Restaurant restaurant = restaurantRepository.findById(restaurantId).get();
        List<OrderItem> orderItemList = new ArrayList<>();
        List<OrderItemDTO> returnValue = new ArrayList<>();

        restaurant.getBill().forEach( bill -> {
            orderItemList.addAll(bill.getOrderItems().stream().filter(  orderItem ->
                                                                        (orderItem.getOrderStatus()== ProgressStatus.READY)||(orderItem.getProductType()== ProductType.WAITERREQUEST))
                                                                        .collect(Collectors.toList()));
        });

        orderItemList.forEach(orderItem -> {
            returnValue.add(dtoUtils.generateOrderItemDTO(orderItem));
        });

        return returnValue;
    }

    public OrderItemDTO changeOrderItem (Long orderItemId,int tempsAjoute){
        OrderItem orderItem = orderItemRepository.findById(orderItemId).get();
        orderItem.setTempsDePreparation(new Date(orderItem.getTempsDePreparation().getTime()+(tempsAjoute*60000)));
        return dtoUtils.generateOrderItemDTO(orderItemRepository.save(orderItem));

    }

}
