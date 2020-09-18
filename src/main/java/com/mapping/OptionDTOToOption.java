package com.mapping;

import com.model.dto.OptionDTO;
import com.model.entity.Option;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OptionDTOToOption {
    OptionDTOToOption instance = Mappers.getMapper(OptionDTOToOption.class);

    @Mapping(target = "checkItemList", ignore = true)
    @Mapping(target = "id", ignore = true)
    Option convert(OptionDTO option);
}
