package com.controller;

import com.model.entity.OrderItem;
import com.model.entity.Request;
import com.model.enums.OrderStatus;
import com.model.enums.RequestType;
import com.repository.OrderItemRepository;
import com.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("rest/kitchen")
public class KitchenRestController {

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    //ALLOW SERVER AND COOK TO LIST REQUEST
    @GetMapping("/request-all")
    public List<Request> findAllRequests() {
        List<Request> requestList = new ArrayList<>();
        requestRepository.findAll().forEach(r -> {
            if (r.getOrderItem().getOrderStatus() != OrderStatus.READY)
                requestList.add(r);
        });

        return requestList;
    }

    //ALLOW COOK TO LIST ORDERS
    @GetMapping("/orders-all")
    public List<Request> findAllOrders() {
        List<Request> orderRequestList = new ArrayList<>();
        requestRepository.findAll().forEach(r -> {
            if (r.getRequestType() == RequestType.FOODREQUEST)
                orderRequestList.add(r);
        });

        return orderRequestList;
    }


    //ALLOW COOK TO MODIFY ORDER TIME OR END ORDER
    @PutMapping("/edit-orderItem")
    public void updateOrderItem(@RequestBody OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }

}
