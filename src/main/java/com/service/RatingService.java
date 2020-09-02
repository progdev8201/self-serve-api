package com.service;

import com.mapping.RateToRateDTOImpl;
import com.model.dto.RateDTO;
import com.model.entity.Product;
import com.model.entity.Rate;
import com.repository.ProductRepository;
import com.repository.RateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RatingService {

    private RateRepository rateRepository;

    private ProductRepository productRepository;

    @Autowired
    public RatingService(RateRepository rateRepository,ProductRepository productRepository) {
        this.rateRepository = rateRepository;
        this.productRepository = productRepository;
    }

    public RateDTO createRate(Rate rate,Product product){
        rate = rateRepository.save(rate);
        if(Objects.isNull(product.getRates())){
            List<Rate> rates = new ArrayList<>();
            rates.add(rate);
            product.setRates(rates);
            productRepository.save(product);

        }
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
