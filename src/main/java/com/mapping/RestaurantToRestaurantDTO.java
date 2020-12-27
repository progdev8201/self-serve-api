package com.mapping;

import com.model.dto.MenuDTO;
import com.model.dto.RestaurantDTO;
import com.model.entity.Menu;
import com.model.entity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RestaurantToRestaurantDTO {
    RestaurantToRestaurantDTO instance = Mappers.getMapper(RestaurantToRestaurantDTO.class);

    @Mapping(target = "bill", ignore = true)
    @Mapping(target = "restaurentTables", ignore = true)
    @Mapping(target = "menus", ignore = true)
    @Mapping(target = "imgFile", ignore = true)
    RestaurantDTO convert(Restaurant restaurant);
}
