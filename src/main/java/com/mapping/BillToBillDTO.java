package com.mapping;

import com.model.dto.BillDTO;
import com.model.entity.Bill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BillToBillDTO {
    BillToBillDTO instance = Mappers.getMapper(BillToBillDTO.class);
    @Mapping(target = "orderCustomer", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "restaurant", ignore = true)
    BillDTO convert(Bill bill);
}
