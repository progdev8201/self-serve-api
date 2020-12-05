package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.dto.*;
import com.model.dto.requests.MenuRequestDTO;
import com.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    @PostMapping("/createMenu")
    public ResponseEntity<?> createMenu(@RequestBody MenuRequestDTO menuRequestDTO) {
        MenuDTO menuDTO =menuService.createMenu(menuRequestDTO.getRestaurantId(), menuRequestDTO.getMenuName(), menuRequestDTO.getMenuType());
        if(Objects.isNull(menuDTO))
            return new ResponseEntity<String>("Fail -> Menu with same name already exists", HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(menuDTO);
    }
    @PutMapping("/updateMenu")
    public ResponseEntity<?> updateMenu(@RequestBody MenuRequestDTO menuRequestDTO) {
        MenuDTO menuDTO =menuService.updateMenu(menuRequestDTO.getMenuId(), menuRequestDTO.getMenuName(), menuRequestDTO.getMenuType());
        return ResponseEntity.ok(menuDTO);
    }
    @DeleteMapping("/deleteMenu/{restaurantId}/{menuId}")
    public ResponseEntity<?> deleteMenu(@PathVariable("restaurantId") Long restaurantId, @PathVariable("menuId") Long menuId) {
        menuService.deleteMenuFromRestaurantList(restaurantId, menuId);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/restaurantName/{ownerId}")
    public ResponseEntity<List<RestaurantSelectionDTO>> findAllRestaurantName(@PathVariable final String ownerId){
        return ResponseEntity.ok(menuService.findAllRestaurantName(ownerId));
    }
}
