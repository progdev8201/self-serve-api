package com.mapping;

import com.model.dto.MenuDTO;
import com.model.entity.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MenuToMenuDTO {
    MenuToMenuDTO instance = Mappers.getMapper(MenuToMenuDTO.class);

    @Mapping(target = "products", ignore = true)
    @Mapping(target = "restaurant", ignore = true)
    @Mapping(target = "speciaux", ignore = true)
    MenuDTO convert(Menu menuDTO);
}
