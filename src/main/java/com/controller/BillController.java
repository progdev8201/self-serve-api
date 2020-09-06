package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.dto.BillDTO;
import com.model.dto.JwtResponse;
import com.model.dto.LoginForm;
import com.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class BillController {

    @Autowired
    ClientService clientService;
    @PostMapping("/makeOrder")
    public ResponseEntity<BillDTO> makeOrder(@RequestBody Map<String, String> json) throws JsonProcessingException {
        BillDTO billDTO = new ObjectMapper().readValue(json.get("bill"),BillDTO.class);
        Long restaurentTableId = new ObjectMapper().readValue(json.get("restaurentTableId"),Long.class);
        return ResponseEntity.ok(clientService.makeOrder(billDTO.getOrderItems(),json.get("guestUsername"),billDTO.getId(),restaurentTableId));
    }

}
