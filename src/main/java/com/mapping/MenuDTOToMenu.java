package com.mapping;

import com.model.dto.MenuDTO;
import com.model.dto.ProductDTO;
import com.model.entity.Menu;
import com.model.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MenuDTOToMenu {
    MenuDTOToMenu instance = Mappers.getMapper(MenuDTOToMenu.class);

    @Mapping(target = "products", ignore = true)
    @Mapping(target = "restaurant", ignore = true)
    Menu convert(MenuDTO menuDTO);
}
