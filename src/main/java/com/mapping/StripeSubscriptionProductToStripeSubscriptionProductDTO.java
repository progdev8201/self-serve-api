package com.mapping;

import com.model.dto.requests.StripeSubscriptionProductsDTO;
import com.model.entity.StripeSubscriptionProducts;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StripeSubscriptionProductToStripeSubscriptionProductDTO {
    StripeSubscriptionProductToStripeSubscriptionProductDTO instance = Mappers.getMapper(StripeSubscriptionProductToStripeSubscriptionProductDTO.class);
    StripeSubscriptionProductsDTO convert(StripeSubscriptionProducts stripeSubscriptionProducts);
}
