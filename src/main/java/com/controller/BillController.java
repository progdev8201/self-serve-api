package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.model.dto.BillDTO;
import com.model.dto.JwtResponse;
import com.model.dto.LoginForm;
import com.model.dto.ProductDTO;
import com.model.enums.BillStatus;
import com.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class BillController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/billStatus/{billId}")
    public BillStatus findBillStatus(@PathVariable final Long billId){
        return clientService.findBillStatus(billId);
    }

    @PostMapping("/makeOrder")
    public ResponseEntity<BillDTO> makeOrder(@RequestBody Map<String, String> json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        BillDTO billDTO = objectMapper.readValue(json.get("billDTO"),BillDTO.class);
        Long restaurentTableId = objectMapper.readValue(json.get("restaurentTableId"),Long.class);
        ProductDTO productDTO = objectMapper.readValue(json.get("productDTO"), ProductDTO.class);
        String commentaire =json.get("commentaire");
        return ResponseEntity.ok(clientService.makeOrder(productDTO,json.get("guestUsername"),billDTO.getId(),restaurentTableId,commentaire));
    }

    @PostMapping("/initBill")
    public ResponseEntity<BillDTO> initBill() {
        return ResponseEntity.ok(clientService.initBill());
    }

    @PostMapping("/makePayment")
    public ResponseEntity<Boolean> makePayment(@RequestBody Map<String, String> json) throws JsonProcessingException {
        Long billId = new ObjectMapper().readValue(json.get("billId"), Long.class);
        return ResponseEntity.ok(clientService.makePayment(billId));
    }

    @PostMapping("/getBill")
    public ResponseEntity<BillDTO> getBill(@RequestBody Map<String, String> json) throws JsonProcessingException {
        Long billId = new ObjectMapper().readValue(json.get("billId"), Long.class);
        return ResponseEntity.ok(clientService.fetchBill(billId));
    }
}
