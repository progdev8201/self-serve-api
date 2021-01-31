package com.mapping;

import com.model.entity.Menu;
import com.model.omnivore.OmnivoreMenu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OmnivoreMenuToMenu {
    OmnivoreMenuToMenu instance = Mappers.getMapper(OmnivoreMenuToMenu.class);

    @Mapping(source = "id", target = "omnivoreMenuId")
    @Mapping(source = "type", target = "omnivoreMenuType")
    @Mapping(target = "id", ignore = true)
    Menu convert(OmnivoreMenu omnivoreMenu);
}
