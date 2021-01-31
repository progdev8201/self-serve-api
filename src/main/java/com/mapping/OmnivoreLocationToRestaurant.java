package com.mapping;

import com.model.dto.BillDTO;
import com.model.entity.Bill;
import com.model.entity.Restaurant;
import com.model.omnivore.OmnivoreLocation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OmnivoreLocationToRestaurant {
    OmnivoreLocationToRestaurant instance = Mappers.getMapper(OmnivoreLocationToRestaurant.class);

    @Mapping(source = "id", target = "locationId")
    @Mapping(source = "name", target = "name")
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "id", ignore = true)
    Restaurant convert(OmnivoreLocation bill);
}
