package com.mapping;

import com.model.dto.CheckItemDTO;
import com.model.dto.ClientDTO;
import com.model.entity.CheckItem;
import com.model.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientToClientDTO {
    ClientToClientDTO instance = Mappers.getMapper(ClientToClientDTO.class);

    ClientDTO convert(Client client);
}
