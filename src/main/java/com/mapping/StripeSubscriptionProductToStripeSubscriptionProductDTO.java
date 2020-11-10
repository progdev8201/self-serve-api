package com.mapping;

import com.model.dto.BillDTO;
import com.model.dto.StripeSubscriptionProductsDTO;
import com.model.entity.Bill;
import com.model.entity.StripeSubscriptionProducts;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StripeSubscriptionProductToStripeSubscriptionProductDTO {
    StripeSubscriptionProductToStripeSubscriptionProductDTO instance = Mappers.getMapper(StripeSubscriptionProductToStripeSubscriptionProductDTO.class);
    StripeSubscriptionProductsDTO convert(StripeSubscriptionProducts stripeSubscriptionProducts);
}
