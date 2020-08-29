package com.mapping;

import com.model.dto.OptionDTO;
import com.model.entity.Option;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OptionToOptionDTO {
    OptionToOptionDTO instance = Mappers.getMapper(OptionToOptionDTO.class);

    @Mapping(target = "checkItemList", ignore = true)
    OptionDTO convert(Option option);
}
