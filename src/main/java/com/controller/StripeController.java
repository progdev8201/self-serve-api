package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mapping.BillDTOToBill;
import com.mapping.OwnerDTOToOwner;
import com.model.dto.BillDTO;
import com.model.dto.OwnerDTO;
import com.model.dto.StripeClientSecretDTO;
import com.model.entity.Owner;
import com.service.ClientService;
import com.service.StripeService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/stripe")
public class StripeController {

    @Autowired
    StripeService stripeService;


    @PostMapping("/createStripeAcccount")
    public ResponseEntity<String> createStripeAccounr(@RequestBody Map<String, String> json) throws JsonProcessingException, StripeException {
        OwnerDTO ownerDTO = new ObjectMapper().readValue(json.get("ownerDto"), OwnerDTO.class);
        return ResponseEntity.ok(stripeService.createStripeAccount(OwnerDTOToOwner.instance.convert(ownerDTO)));
    }

    @PostMapping("/fetchPaymentIntent")
    public ResponseEntity<StripeClientSecretDTO> processPayment(@RequestBody Map<String, String> json) throws JsonProcessingException, StripeException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        BillDTO billDTO = objectMapper.readValue(json.get("billDTO"), BillDTO.class);
        String restaurentStripeAccount =json.get("restaurentStripeAccount");
        return ResponseEntity.ok(stripeService.processPayment(restaurentStripeAccount, BillDTOToBill.instance.convert(billDTO)));
    }
    @PostMapping("/fetchPaymentRquestPaymentIntent")
    public ResponseEntity<StripeClientSecretDTO> processPaymentRequestPayment(@RequestBody Map<String, String> json) throws JsonProcessingException, StripeException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        BillDTO billDTO = objectMapper.readValue(json.get("billDTO"), BillDTO.class);
        String restaurentStripeAccount =json.get("restaurentStripeAccount");
        return ResponseEntity.ok(stripeService.processRequestPayment(restaurentStripeAccount, BillDTOToBill.instance.convert(billDTO)));
    }
    @GetMapping ("/.well-known/apple-developer-merchantid-domain-association")
    public ResponseEntity<?> returnDomainFile() throws IOException {
        return ResponseEntity.ok(stripeService.returnDomainFile());
    }
}
