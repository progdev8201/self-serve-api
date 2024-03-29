package com.mapping;

import com.model.dto.GuestDTO;
import com.model.entity.Guest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GuestToGuestDTO {
    GuestToGuestDTO instance = Mappers.getMapper(GuestToGuestDTO.class);

    @Mapping(target = "bills", ignore = true)
    GuestDTO convert(Guest guestDTO);
}
