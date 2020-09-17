package com.mapping;

import com.model.dto.CheckItemDTO;
import com.model.entity.CheckItem;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CheckItemDTOCheckItem {
    CheckItemDTOCheckItem instance = Mappers.getMapper(CheckItemDTOCheckItem.class);

    CheckItem convert(CheckItemDTO checkItem);
}
