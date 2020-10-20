package com.service;

import com.model.entity.Product;
import com.model.entity.Rate;
import com.repository.RateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
// TODO: all test should include assert arrange act as comments so its easier to understand code
@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    private RatingService ratingService;
    @Mock
    RateRepository rateRepository;

    @Test
    public void testerChercherNoteMoyenneProduit3Item(){
        ratingService = new RatingService(rateRepository,null,null);
        List<Rate> rateList = new ArrayList<>();
        Rate rate = new Rate();
        rate.setRate(5);
        rateList.add(rate);
        Rate rate2 = new Rate();
        rate2.setRate(1);
        rateList.add(rate2);
        Rate rate3 = new Rate();
        rate3.setRate(3);
        rateList.add(rate3);

        Product product = new Product();
        product.setRates(rateList);


        assertEquals(3,ratingService.findAverageRate(product));

    }
    @Test
    public void testerChercherNoteMoyenneProduit2Item(){
        ratingService = new RatingService(rateRepository,null,null);
        List<Rate> rateList = new ArrayList<>();
        Rate rate = new Rate();
        rate.setRate(5);
        rateList.add(rate);
        Rate rate2 = new Rate();
        rate2.setRate(3);
        rateList.add(rate2);


        Product product = new Product();
        product.setRates(rateList);


        assertEquals(4,ratingService.findAverageRate(product));

    }

    @Test
    public void testerChercherNoteMoyenneProduitPasDeRateRetourne0(){
        ratingService = new RatingService(rateRepository,null,null);


        Product product = new Product();


        assertEquals(0,ratingService.findAverageRate(product));

    }


}