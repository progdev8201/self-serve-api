package com.mapping;

import com.model.dto.OwnerDTO;
import com.model.entity.Owner;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OwnerToOwnerDTO {
    OwnerToOwnerDTO instance = Mappers.getMapper(OwnerToOwnerDTO.class);

    OwnerDTO convert(Owner orderOwner);
}
