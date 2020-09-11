package com.mapping;

import com.model.dto.BillDTO;
import com.model.entity.Bill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BillDTOToBill {
    BillDTOToBill instance = Mappers.getMapper(BillDTOToBill.class);
    @Mapping(target = "orderCustomer", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "restaurant", ignore = true)
    Bill convert(BillDTO bill);
}
