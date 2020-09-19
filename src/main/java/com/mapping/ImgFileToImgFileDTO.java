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

    ImgFileDTO convert(ImgFile imgFile);
}
