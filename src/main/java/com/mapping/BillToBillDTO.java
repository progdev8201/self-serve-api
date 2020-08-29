package com.mapping;

import com.model.dto.BillDTO;
import com.model.entity.Bill;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BillToBillDTO {
    BillToBillDTO instance = Mappers.getMapper(BillToBillDTO.class);

    BillDTO convert(Bill bill);
}
