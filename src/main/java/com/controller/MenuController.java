package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.dto.BillDTO;
import com.model.dto.MenuDTO;
import com.service.ClientService;
import com.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    MenuService menuService;
    @PostMapping("/changeFeatured")
    public ResponseEntity<MenuDTO> changeFeatured (@RequestBody Map<String, String> json) throws JsonProcessingException {
        MenuDTO menuDTO = new ObjectMapper().readValue(json.get("menu"),MenuDTO.class);
        return ResponseEntity.ok(menuService.createSpecial(menuDTO,menuDTO.getProducts()));
    }

    @PostMapping("/removeFeatured")
    public ResponseEntity<MenuDTO> removeFeatured(@RequestBody Map<String, String> json) throws JsonProcessingException {
        MenuDTO menuDTO = new ObjectMapper().readValue(json.get("menu"),MenuDTO.class);
        return ResponseEntity.ok(menuService.removeSpecial(menuDTO,menuDTO.getProducts()));
    }
    @PostMapping("/getMenu")
    public ResponseEntity<MenuDTO> getMenu(@RequestBody Map<String, String> json) throws JsonProcessingException {
        Long menuId = new ObjectMapper().readValue(json.get("menuId"),Long.class);
        return ResponseEntity.ok(menuService.findMenu(menuId));
    }
}
