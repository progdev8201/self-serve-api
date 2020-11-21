package com.mapping;

import com.model.dto.OwnerDTO;
import com.model.entity.Owner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OwnerToOwnerDTO {
    OwnerToOwnerDTO instance = Mappers.getMapper(OwnerToOwnerDTO.class);

    @Mapping(target = "bills", ignore = true)
    @Mapping(target = "restaurants", ignore = true)
    @Mapping(target = "password",ignore = true)
    OwnerDTO convert(Owner orderOwner);
}
