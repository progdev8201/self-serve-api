package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.dto.*;
import com.service.ClientService;
import com.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    MenuService menuService;

    @PostMapping("/getMenu")
    public ResponseEntity<List<MenuDTO>> getMenu(@RequestBody Map<String, String> json) throws JsonProcessingException {
        Long restaurantId = new ObjectMapper().readValue(json.get("restaurantId"),Long.class);
        return ResponseEntity.ok(menuService.findAllMenuForRestaurants(restaurantId));
    }

    @GetMapping("/restaurantName/{ownerId}")
    public ResponseEntity<List<RestaurantSelectionDTO>> findAllRestaurantName(@PathVariable final String ownerId){
        return ResponseEntity.ok(menuService.findAllRestaurantName(ownerId));
    }
}
