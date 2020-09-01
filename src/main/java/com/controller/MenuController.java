package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.dto.BillDTO;
import com.model.dto.MenuDTO;
import com.service.ClientService;
import com.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    MenuService menuService;
    @PostMapping("/changeFeatured")
    public ResponseEntity<MenuDTO> makeOrder(@RequestBody Map<String, String> json) throws JsonProcessingException {
        MenuDTO menuDTO = new ObjectMapper().readValue(json.get("menu"),MenuDTO.class);
        return ResponseEntity.ok(menuService.createSpecial(menuDTO,menuDTO.getProducts()));
    }

    @PostMapping("/removeFeatured")
    public ResponseEntity<MenuDTO> removeFeatured(@RequestBody Map<String, String> json) throws JsonProcessingException {
        MenuDTO menuDTO = new ObjectMapper().readValue(json.get("menu"),MenuDTO.class);
        return ResponseEntity.ok(menuService.removeSpecial(menuDTO,menuDTO.getProducts()));
    }
}