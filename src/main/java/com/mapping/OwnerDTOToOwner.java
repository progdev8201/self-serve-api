package com.mapping;

import com.model.dto.OwnerDTO;
import com.model.entity.Owner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OwnerDTOToOwner {
    OwnerDTOToOwner instance = Mappers.getMapper(OwnerDTOToOwner.class);

    @Mapping(target = "bills", ignore = true)
    @Mapping(target = "restaurantList", ignore = true)
    Owner convert(OwnerDTO orderOwner);
}
