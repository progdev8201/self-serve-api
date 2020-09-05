package com.mapping;

import com.model.dto.BillDTO;
import com.model.dto.RestaurentTableDTO;
import com.model.entity.Bill;
import com.model.entity.RestaurentTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RestaurentTableToRestaurenTableDTO {
    RestaurentTableToRestaurenTableDTO instance = Mappers.getMapper(RestaurentTableToRestaurenTableDTO.class);
    @Mapping(target = "billDTOList", ignore = true)
    @Mapping(target = "restaurant", ignore = true)
    RestaurentTableDTO convert(RestaurentTable restaurentTable);
}
