package com.mapping;

import com.model.dto.CheckItemDTO;
import com.model.entity.CheckItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CheckItemToCheckItem {
    CheckItemToCheckItem instance = Mappers.getMapper(CheckItemToCheckItem.class);

    CheckItem convert(CheckItem checkItem);
}
