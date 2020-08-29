package com.mapping;

import com.model.dto.MenuDTO;
import com.model.entity.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MenuToMenuDTO {
    MenuToMenuDTO instance = Mappers.getMapper(MenuToMenuDTO.class);

    MenuDTO convert(Menu menuDTO);
}
