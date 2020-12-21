package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.zxing.WriterException;
import com.model.dto.OrderItemDTO;
import com.model.dto.RestaurantDTO;
import com.model.dto.RestaurantEmployerDTO;
import com.model.dto.RestaurentTableDTO;
import com.service.KitchenService;
import com.service.RestaurentTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("rest/kitchen")
public class KitchenRestController {

    @Autowired
    private RestaurentTableService restaurentTableService;

    @Autowired
    private KitchenService kitchenService;

    //GET

    @PreAuthorize("hasAnyAuthority('ROLE_OWNER','ROLE_ADMIN')")
    @GetMapping("/restaurantEmployers/{restaurantId}")
    public ResponseEntity<List<RestaurantEmployerDTO>> findAllRestaurantEmployers(@PathVariable final Long restaurantId) {
        return kitchenService.findAllRestaurantEmployers(restaurantId);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_OWNER','ROLE_ADMIN','ROLE_COOK','ROLE_WAITER')")
    @GetMapping("/restaurantEmployer/{username}")
    public RestaurantEmployerDTO findRestaurantEmployer(@PathVariable final String username) {
        return kitchenService.findRestaurantEmployer(username);
    }

    @GetMapping("/findRestaurantByRestaurantTableId/{tableID}")
    public ResponseEntity<RestaurantDTO> findRestaurantByRestaurantTableId(@PathVariable long tableID) {
        return ResponseEntity.ok(kitchenService.findRestaurantByRestaurantTableId(tableID));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_OWNER','ROLE_ADMIN','ROLE_COOK','ROLE_WAITER')")
    @GetMapping("/employerRestaurant/{username:.+}")
    public Long findEmployerRestaurantId(@PathVariable final String username) {
        return kitchenService.findEmployerRestaurantId(username);
    }

    //ALLOW COOK TO MODIFY ORDER TIME OR END ORDER
    //PUT
    @PreAuthorize("hasAnyAuthority('ROLE_COOK','ROLE_WAITER')")
    @PutMapping("/editOrderItem")
    public OrderItemDTO updateOrderItem(@RequestBody OrderItemDTO orderItemDTO) {
        return kitchenService.updateOrderItem(orderItemDTO);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_OWNER','ROLE_ADMIN')")
    @PutMapping("/updateRestaurantUser")
    public ResponseEntity<String> updateRestaurantEmployee(@RequestBody final RestaurantEmployerDTO restaurantEmployerDTO) {
        return kitchenService.updateRestaurantEmployee(restaurantEmployerDTO);
    }

    //POST

    @PreAuthorize("hasAnyAuthority('ROLE_WAITER','ROLE_COOK')")
    @PostMapping("/findAllTables")
    public ResponseEntity<List<RestaurentTableDTO>> findAllTables(@RequestBody Map<String, String> json) throws JsonProcessingException {
        Long restaurentId = new ObjectMapper().readValue(json.get("restaurentId"), Long.class);
        return restaurentTableService.findAllForRestaurent(restaurentId);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_OWNER','ROLE_ADMIN')")
    @PostMapping("/createRestaurant")
    public ResponseEntity<RestaurantDTO> createRestaurant(@RequestBody Map<String, String> json) throws IOException, WriterException {
        ObjectMapper objectMapper = new ObjectMapper();
        String ownerUsername = json.get("ownerUsername");
        String restaurantName = json.get("restaurantName");
        int nombreDeTable = objectMapper.readValue(json.get("nombreDeTable"), Integer.class);

        return ResponseEntity.ok(kitchenService.createRestaurant(ownerUsername, restaurantName, nombreDeTable));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_OWNER','ROLE_ADMIN')")
    @PostMapping("/logo/{restaurantId}")
    public ResponseEntity<?> saveProductImg(@RequestParam("file") MultipartFile file, @PathVariable long restaurantId) throws IOException {
        return ResponseEntity.ok(kitchenService.uploadLogo(file, restaurantId));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_OWNER','ROLE_ADMIN')")
    @PostMapping("/deleteRestaurant")
    public ResponseEntity deleteRestaurant(@RequestBody Map<String, String> json) throws JsonProcessingException {
        Long restaurantId = new ObjectMapper().readValue(json.get("restaurantId"), Long.class);
        return kitchenService.deleteRestaurant(restaurantId);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_OWNER','ROLE_ADMIN')")
    @PostMapping("/addTable/{restaurantId}/{tableAmount}")
    public ResponseEntity<RestaurantDTO> addRestaurantTable(@PathVariable long restaurantId, @PathVariable int tableAmount) throws IOException, WriterException {
        return kitchenService.addRestaurantTable(restaurantId, tableAmount);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_OWNER','ROLE_ADMIN')")
    @PostMapping("/modifierNomTable")
    public ResponseEntity<RestaurantDTO> updateRestaurantName(@RequestBody Map<String, String> json) throws JsonProcessingException {
        String restaurantName = json.get("restaurantName");
        Long restaurantId = new ObjectMapper().readValue(json.get("restaurantId"), Long.class);
        return kitchenService.modifierRestaurantName(restaurantName, restaurantId);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_OWNER','ROLE_ADMIN')")
    @PostMapping("/deleteTable")
    public ResponseEntity deleteRestaurantTable(@RequestBody Map<String, String> json) throws JsonProcessingException {
        Long tableId = new ObjectMapper().readValue(json.get("restaurantTableId"), Long.class);
        Long restaurantId = new ObjectMapper().readValue(json.get("restaurantId"), Long.class);
        kitchenService.deleteRestaurantTable(tableId, restaurantId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_WAITER')")
    @PostMapping("/getWaiterRequest")
    public ResponseEntity<List<OrderItemDTO>> getWaiterRequests(@RequestBody Map<String, String> json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.registerModule(new JavaTimeModule());
        Long restaurentId = mapper.readValue(json.get("restaurentId"), Long.class);
        return kitchenService.fetchWaiterRequest(restaurentId);
    }

    @PreAuthorize("hasAuthority('ROLE_COOK')")
    @PostMapping("/changeOrderItemTime")
    public ResponseEntity<OrderItemDTO> changeOrderItemTime(@RequestBody Map<String, String> json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.registerModule(new JavaTimeModule());
        Long orderItemId = mapper.readValue(json.get("orderItemId"), Long.class);
        int tempsAjoute = mapper.readValue(json.get("tempsAjoute"), Integer.class);
        return ResponseEntity.ok(kitchenService.changeOrderItem(orderItemId, tempsAjoute));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_OWNER','ROLE_ADMIN')")
    @PostMapping("/addUserToRestaurant")
    public ResponseEntity<String> addUserToRestaurant(@RequestBody final RestaurantEmployerDTO restaurantEmployerDTO) {
        return kitchenService.addUserToRestaurant(restaurantEmployerDTO);
    }

}
