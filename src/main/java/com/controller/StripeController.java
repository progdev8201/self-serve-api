package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mapping.BillDTOToBill;
import com.model.dto.*;
import com.model.dto.requests.*;
import com.service.StripeService;
import com.stripe.exception.StripeException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stripe")
public class StripeController {

    @Autowired
    StripeService stripeService;

    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    @PostMapping("/createStripeAcccount")
    public ResponseEntity<StripeCreateAccountUrlDTO> createStripeAccount(@RequestBody Map<String, String> json) throws JsonProcessingException, StripeException {
        return ResponseEntity.ok(stripeService.createStripeAccount(json.get("username")));
    }

    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    @PostMapping("/saveAccountId")
    public ResponseEntity<?> saveOwnerAccountId(@RequestBody Map<String, String> json){
        String username= json.get("username");
        stripeService.saveStripeAccountId(username);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_GUEST','ROLE_CLIENT')")
    @PostMapping("/getAccountId")
    public ResponseEntity<StripeAccountIdDTO> getStripeAccountId(@RequestBody Map<String,String> json) throws JsonProcessingException {
        Long menuId = new ObjectMapper().readValue(json.get("restaurantId"),Long.class);
        return ResponseEntity.ok(stripeService.getAccountId(menuId));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_GUEST','ROLE_CLIENT')")
    @PostMapping("/fetchPaymentIntent")
    public ResponseEntity<StripeClientSecretDTO> processPayment(@RequestBody Map<String, String> json) throws JsonProcessingException, StripeException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        BillDTO billDTO = objectMapper.readValue(json.get("billDTO"), BillDTO.class);
        String restaurentStripeAccount =json.get("restaurentStripeAccount");
        return ResponseEntity.ok(stripeService.processPayment(restaurentStripeAccount, BillDTOToBill.instance.convert(billDTO)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_GUEST','ROLE_CLIENT')")
    @PostMapping("/fetchPaymentRquestPaymentIntent")
    public ResponseEntity<StripeClientSecretDTO> processPaymentRequestPayment(@RequestBody Map<String, String> json) throws JsonProcessingException, StripeException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        BillDTO billDTO = objectMapper.readValue(json.get("billDTO"), BillDTO.class);
        String restaurentStripeAccount =json.get("restaurentStripeAccount");
        return ResponseEntity.ok(stripeService.processRequestPayment(restaurentStripeAccount, BillDTOToBill.instance.convert(billDTO)));
    }

    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    @GetMapping("/fetchSubscriptionProducts")
    public ResponseEntity<List<StripeSubscriptionProductsDTO>> fetchSubscriptionProducts(){
        return ResponseEntity.ok(stripeService.fetchSubscriptionProducts());
    }

    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    @PostMapping("/fetchSubscriptionSession")
    public ResponseEntity<StripeSessionCustomerIdDTO> processSubscriptionSession (@RequestBody Map <String ,String > json) throws JsonProcessingException, StripeException  {
        String ownerEmail = json.get("ownerEmail");
        return ResponseEntity.ok(stripeService.createCheckoutSession(ownerEmail));
    }

    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    @PostMapping("/createSubscription")
    public ResponseEntity<SubscriptionEntityDTO> createSubscription(@RequestBody SubscriptionRequestDTO subscriptionRequestDTO) throws StripeException, JSONException {
        return ResponseEntity.ok(stripeService.createSubscription(subscriptionRequestDTO));
    }

    //permet de mettre a jour la methode de payment si jamais la methode de payment fail;
    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    @PostMapping("/retrySubscription")
    public ResponseEntity<SubscriptionEntityDTO> retrySubscriptionPaymentMethod(@RequestBody SubscriptionRequestDTO subscriptionRequestDTO) throws Exception {
        return ResponseEntity.ok(stripeService.retryInvoice(subscriptionRequestDTO));
    }

    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    @PostMapping("/cancelSubscription")
    public ResponseEntity<SubscriptionEntityDTO> cancelSubscription(@RequestBody RetreiveSubscriptionRequestDTO retreiveSubscriptionRequestDTO) throws StripeException {
        return ResponseEntity.ok(stripeService.cancelSubscription(retreiveSubscriptionRequestDTO.getOwnerEmail()));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_OWNER','ROLE_WAITER','ROLE_COOK')")
    @PostMapping("/retreiveSubscription")
    public ResponseEntity<SubscriptionEntityDTO> retreiveSubscription(@RequestBody RetreiveSubscriptionRequestDTO retreiveSubscriptionRequestDTO) throws StripeException, JSONException {
        return ResponseEntity.ok(stripeService.retreiveSubscription(retreiveSubscriptionRequestDTO));
    }

    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    @GetMapping ("/.well-known/apple-developer-merchantid-domain-association")
    public ResponseEntity<?> returnDomainFile() throws IOException {
        return ResponseEntity.ok(stripeService.returnDomainFile());
    }
}
