package com.mapping;

import com.model.dto.CustomPropretyDTO;
import com.model.entity.CustomProprety;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomPropretyToCustomPropretyDTO {
    CustomPropretyToCustomPropretyDTO instance = Mappers.getMapper(CustomPropretyToCustomPropretyDTO.class);

    CustomProprety convert(CustomPropretyDTO customPropretyDTO);
}
