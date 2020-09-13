package com.service;

import com.model.dto.OwnerDTO;
import com.model.entity.Owner;
import com.stripe.exception.StripeException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles ("dev")
class StripeServiceTest {
    @Autowired
    StripeService stripeService;


    @Test
    public void creeStripeAccount() throws StripeException {
        Owner owner = new Owner();
        owner.setId(2L);
        stripeService.createStripeAccount(owner);
    }
}