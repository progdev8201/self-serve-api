package com.mapping;

import com.model.dto.CheckItemDTO;
import com.model.entity.CheckItem;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CheckItemToCheckItemDTO {
    CheckItemToCheckItemDTO instance = Mappers.getMapper(CheckItemToCheckItemDTO.class);

    CheckItemDTO convert(CheckItem checkItem);
}
