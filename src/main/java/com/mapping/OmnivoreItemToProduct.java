package com.mapping;

import com.model.entity.Product;
import com.model.omnivore.OmnivoreItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OmnivoreItemToProduct {
    OmnivoreItemToProduct instance = Mappers.getMapper(OmnivoreItemToProduct.class);

    @Mapping(target = "id",ignore = true)
    @Mapping(source = "id", target = "omnivoreItemId")
    @Mapping(source = "pricePerUnit", target = "prix")
    Product convert(OmnivoreItem omnivoreItem);
}
