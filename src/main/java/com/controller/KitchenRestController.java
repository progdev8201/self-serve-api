package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.dto.OrderItemDTO;
import com.model.dto.RestaurentTableDTO;
import com.model.entity.OrderItem;
import com.model.entity.Request;
import com.model.enums.OrderStatus;
import com.model.enums.ProgressStatus;
import com.model.enums.RequestType;
import com.repository.OrderItemRepository;
import com.repository.RequestRepository;
import com.service.KitchenService;
import com.service.RestaurentTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("rest/kitchen")
public class KitchenRestController {

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    private RestaurentTableService restaurentTableService;

    @Autowired
    private KitchenService kitchenService;


    //ALLOW SERVER AND COOK TO LIST REQUEST
    @GetMapping("/request-all")
    public List<Request> findAllRequests() {
        List<Request> requestList = new ArrayList<>();
        requestRepository.findAll().forEach(r -> {
            if (r.getOrderItem().getOrderStatus() != ProgressStatus.READY)
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

    @PostMapping("/findAllTables")
    public ResponseEntity<List<RestaurentTableDTO>> findAllTables(@RequestBody Map<String, String> json) throws JsonProcessingException {
        Long restaurentId = new ObjectMapper().readValue(json.get("restaurentId"),Long.class);
        return ResponseEntity.ok(restaurentTableService.findAllForRestaurent(restaurentId));
    }



    @PostMapping("/changeOrderItemStatus")
    public ResponseEntity changeOrderItemStatus(@RequestBody Map<String, String> json) throws JsonProcessingException {
        OrderItemDTO orderItemDTO = new ObjectMapper().readValue(json.get("orderItem"), OrderItemDTO.class);
        kitchenService.changeOrderItemStatus(orderItemDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

}
