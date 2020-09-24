package com.service;

import com.mapping.RateToRateDTOImpl;
import com.model.dto.RateDTO;
import com.model.entity.Product;
import com.model.entity.Rate;
import com.repository.ProductRepository;
import com.repository.RateRepository;
import com.service.DtoUtil.DTOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class RatingService {


    private RateRepository rateRepository;

    private ProductRepository productRepository;
    DTOUtils dtoUtils;

    @Autowired
    public RatingService(RateRepository rateRepository, ProductRepository productRepository,DTOUtils dtoUtils) {
        this.rateRepository = rateRepository;
        this.productRepository = productRepository;
        this.dtoUtils = dtoUtils;
    }

    public RateDTO createRate(Rate rate, Long productId) {
        rate = rateRepository.save(rate);
        Product product = productRepository.findById(productId).get();
        if (Objects.isNull(product.getRates()))
            product.setRates(new ArrayList<>());


        product.getRates().add(rate);
        productRepository.save(product);
        return dtoUtils.generateRateDTO(rateRepository.save(rate));
    }

    public double findAverageRate(Product product) {
        if (Objects.nonNull(product.getRates())) {
            return product.getRates()
                    .stream()
                    .map(Rate::getRate)
                    .collect(Collectors.summarizingDouble(Double::doubleValue))
                    .getAverage();

        }
        return 0;
    }

}
