package com.service;

import com.mapping.RateToRateDTOImpl;
import com.model.dto.RateDTO;
import com.model.entity.Product;
import com.model.entity.Rate;
import com.repository.RateRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;
import java.util.stream.Collectors;

public class RatingService {

    private RateRepository rateRepository;

    @Autowired
    public RatingService(RateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    public RateDTO createRate(Rate rate){
        return RateToRateDTOImpl.instance.convert(rateRepository.save(rate));
    }
    public double findAverageRate(Product product){
        if(Objects.nonNull(product.getRates()))
        {
            return    product.getRates()
                    .stream()
                    .map(Rate::getRate)
                    .collect(Collectors.summarizingDouble(Double::doubleValue))
                    .getAverage();

        }
        return 0;
    }

}
