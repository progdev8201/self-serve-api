package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.zxing.WriterException;
import com.model.dto.MenuDTO;
import com.model.dto.OrderItemDTO;
import com.model.dto.RestaurantDTO;
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

import java.io.IOException;
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
        Long restaurentId = new ObjectMapper().readValue(json.get("restaurentId"), Long.class);
        return ResponseEntity.ok(restaurentTableService.findAllForRestaurent(restaurentId));
    }

    @PostMapping("/createRestaurant")
    public ResponseEntity<RestaurantDTO> createRestaurant(@RequestBody Map<String, String> json) throws IOException, WriterException {
        ObjectMapper objectMapper = new ObjectMapper();
        String ownerUsername = json.get("ownerUsername");
        String restaurantName = json.get("restaurantName");
        int nombreDeTable = objectMapper.readValue(json.get("nombreDeTable"), Integer.class);

        return ResponseEntity.ok(kitchenService.createRestaurant(ownerUsername, restaurantName, nombreDeTable));

    }

    @PostMapping("/deleteRestaurant")
    public ResponseEntity deleteRestaurant(@RequestBody Map<String, String> json) throws JsonProcessingException {
        Long restaurantId = new ObjectMapper().readValue(json.get("restaurantId"),Long.class);
        kitchenService.deleteRestaurant(restaurantId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/addTable")
    public ResponseEntity<RestaurantDTO> addRestaurantTable(@RequestBody Map<String,String> json) throws IOException, WriterException {
        Long restaurantId = new ObjectMapper().readValue(json.get("restaurantId"),Long.class);
        return ResponseEntity.ok(kitchenService.addRestaurantTable(restaurantId));
    }

    @PostMapping("/modifierNomTable")
    public ResponseEntity<RestaurantDTO> updateRestaurantName(@RequestBody Map<String,String> json) throws JsonProcessingException {
        String restaurantName = json.get("restaurantName");
        Long restaurantId = new ObjectMapper().readValue(json.get("restaurantId"),Long.class);
        return ResponseEntity.ok(kitchenService.modifierRestaurantName(restaurantName,restaurantId));
    }
    @PostMapping("/deleteTable")
    public ResponseEntity deleteRestaurantTble(@RequestBody Map<String,String> json) throws JsonProcessingException {
        Long tableId = new ObjectMapper().readValue(json.get("restaurantTableId"),Long.class);
        Long restaurantId = new ObjectMapper().readValue(json.get("restaurantId"),Long.class);
        kitchenService.deleteRestaurantTable(tableId,restaurantId);
        return  new ResponseEntity(HttpStatus.OK);
    }
    @PostMapping("/changeOrderItemStatus")
    public ResponseEntity<OrderItemDTO> changeOrderItemStatus(@RequestBody Map<String, String> json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.registerModule(new JavaTimeModule());
        OrderItemDTO orderItemDTO = mapper.readValue(json.get("orderItemDTO"), OrderItemDTO.class);
        return ResponseEntity.ok(kitchenService.changeOrderItemStatus(orderItemDTO));
    }

    @PostMapping("/getWaiterRequest")
    public ResponseEntity<List<OrderItemDTO>> getWaiterRequests(@RequestBody Map<String, String> json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.registerModule(new JavaTimeModule());
        Long restaurentId = mapper.readValue(json.get("restaurentId"), Long.class);
        return ResponseEntity.ok(kitchenService.fetchWaiterRequest(restaurentId));
    }


    @PostMapping("/changeOrderItemTime")
    public ResponseEntity<OrderItemDTO> changeOrderItemTime(@RequestBody Map<String, String> json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.registerModule(new JavaTimeModule());
        Long orderItemId = mapper.readValue(json.get("orderItemId"), Long.class);
        int tempsAjoute = mapper.readValue(json.get("tempsAjoute"), Integer.class);
        return ResponseEntity.ok(kitchenService.changeOrderItem(orderItemId, tempsAjoute));
    }

    @PostMapping("/findMenuByRestaurantId")
    public ResponseEntity<MenuDTO> findMenuByRestaurantId(@RequestBody Map<String,String> json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Long restaurantIdDTO = mapper.readValue(json.get("restaurantTableId"),Long.class);
        return ResponseEntity.ok(kitchenService.menuParRestaurantTable(restaurantIdDTO));
    }


}
