package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapping.ProductDTOToProduct;
import com.model.dto.MenuDTO;
import com.model.dto.ProductDTO;
import com.model.dto.RateDTO;
import com.model.entity.Menu;
import com.model.entity.Product;
import com.model.entity.Rate;
import com.repository.MenuRepository;
import com.repository.ProductRepository;
import com.repository.RateRepository;
import com.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rate")
public class RateController {

    @Autowired
    RatingService ratingService;

    @PostMapping("/createRate")
    public ResponseEntity<RateDTO> find(@RequestBody Map<String, String> json) throws JsonProcessingException {
        Rate rate = new ObjectMapper().readValue(json.get("rate"),Rate.class);
        Product product = new ObjectMapper().readValue(json.get("product"),Product.class);
        return ResponseEntity.ok(ratingService.createRate(rate,product));
    }




}
