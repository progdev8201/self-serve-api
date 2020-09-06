package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.dto.RateDTO;
import com.model.dto.RestaurentTableDTO;
import com.model.entity.Product;
import com.model.entity.Rate;
import com.model.entity.RestaurentTable;
import com.service.RestaurentTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/restaurantTable")
public class RestaurantTableController {
    @Autowired
    private RestaurentTableService restaurentTableService;

    @PostMapping("/createRate")
    public ResponseEntity<List<RestaurentTableDTO>> find(@RequestBody Map<String, String> json) throws JsonProcessingException {
        Long restaurentId = new ObjectMapper().readValue(json.get("restaurentId"),Long.class);
        return ResponseEntity.ok(restaurentTableService.findAllForRestaurent(restaurentId));
    }
}
