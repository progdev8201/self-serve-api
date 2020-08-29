package com.mapping;

import com.model.dto.EmployeeDTO;
import com.model.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployeeToEmployeeDTO {
    EmployeeToEmployeeDTO instance = Mappers.getMapper(EmployeeToEmployeeDTO.class);

    EmployeeDTO convert(Employee employee);
}
