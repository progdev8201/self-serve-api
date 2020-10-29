package com.mapping;

import com.model.dto.CookDTO;
import com.model.entity.Cook;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployeeToEmployeeDTO {
    EmployeeToEmployeeDTO instance = Mappers.getMapper(EmployeeToEmployeeDTO.class);
    @Mapping(target = "bills", ignore = true)
    CookDTO convert(Cook cook);
}
