package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.dto.*;
import com.model.dto.requests.MenuRequestDTO;
import com.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;


    // Post

    @PreAuthorize("hasAnyAuthority('ROLE_OWNER','ROLE_ADMIN','ROLE_GUEST','ROLE_CLIENT')")
    @PostMapping("/getMenu")
    public ResponseEntity<List<MenuDTO>> getMenu(@RequestBody Map<String, String> json) throws JsonProcessingException {
        Long restaurantId = new ObjectMapper().readValue(json.get("restaurantId"),Long.class);
        return ResponseEntity.ok(menuService.findFoodMenuForRestaurants(restaurantId));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_OWNER','ROLE_ADMIN')")
    @PostMapping("/getAllMenu")
    public ResponseEntity<List<MenuDTO>> getAllMenu(@RequestBody Map<String, String> json) throws JsonProcessingException {
        Long restaurantId = new ObjectMapper().readValue(json.get("restaurantId"),Long.class);
        return menuService.findAllMenuForRestaurants(restaurantId);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_OWNER','ROLE_ADMIN')")
    @PostMapping("/createMenu")
    public ResponseEntity<?> createMenu(@RequestBody MenuRequestDTO menuRequestDTO) {
        return menuService.createMenu(menuRequestDTO.getRestaurantId(), menuRequestDTO.getMenuName(), menuRequestDTO.getMenuType());
    }

    // Put

    @PreAuthorize("hasAnyAuthority('ROLE_OWNER','ROLE_ADMIN')")
    @PutMapping("/updateMenu")
    public ResponseEntity<?> updateMenu(@RequestBody MenuRequestDTO menuRequestDTO) {
        MenuDTO menuDTO =menuService.updateMenu(menuRequestDTO.getMenuId(), menuRequestDTO.getMenuName(), menuRequestDTO.getMenuType());
        return ResponseEntity.ok(menuDTO);
    }

    // Get

    @PreAuthorize("hasAnyAuthority('ROLE_OWNER','ROLE_ADMIN')")
    @GetMapping("/restaurantName/{ownerId}")
    public ResponseEntity<List<RestaurantSelectionDTO>> findAllRestaurantName(@PathVariable final String ownerId){
        return ResponseEntity.ok(menuService.findAllRestaurantName(ownerId));
    }

    // Delete

    @PreAuthorize("hasAnyAuthority('ROLE_OWNER','ROLE_ADMIN')")
    @DeleteMapping("/deleteMenu/{restaurantId}/{menuId}")
    public ResponseEntity<?> deleteMenu(@PathVariable("restaurantId") Long restaurantId, @PathVariable("menuId") Long menuId) {
        return menuService.deleteMenuFromRestaurantList(restaurantId, menuId);
    }
}
