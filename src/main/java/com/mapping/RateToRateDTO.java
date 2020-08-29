package com.mapping;

import com.model.dto.RateDTO;
import com.model.entity.Rate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RateToRateDTO {
    RateToRateDTO instance = Mappers.getMapper(RateToRateDTO.class);

    RateDTO convert(Rate rate);
}
