package com.mapping;

import com.model.entity.Restaurant;
import com.model.entity.RestaurentTable;
import com.model.omnivore.OmnivoreLocation;
import com.model.omnivore.OmnivoreTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OmnivoreTableToRestaurantTable {
    OmnivoreTableToRestaurantTable instance = Mappers.getMapper(OmnivoreTableToRestaurantTable.class);
    @Mapping(source = "id",target = "omnivoreTableID")
    @Mapping(source="number",target = "tableNumber")
    RestaurentTable convert(OmnivoreTable bill);
}
