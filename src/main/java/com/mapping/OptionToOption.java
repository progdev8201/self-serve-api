package com.mapping;

import com.model.dto.OptionDTO;
import com.model.entity.Option;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OptionToOption {
    OptionToOption instance = Mappers.getMapper(OptionToOption.class);

    @Mapping(target = "checkItemList", ignore = true)
    Option convert(Option option);
}
