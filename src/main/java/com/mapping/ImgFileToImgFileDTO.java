package com.mapping;

import com.model.dto.ImgFileDTO;
import com.model.dto.ProductDTO;
import com.model.entity.ImgFile;
import com.model.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ImgFileToImgFileDTO {
    ImgFileToImgFileDTO instance = Mappers.getMapper(ImgFileToImgFileDTO.class);
    @Mapping(target = "data", ignore = true)
    ImgFileDTO convert(ImgFile imgFile);
}
