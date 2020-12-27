package com.mapping;

import com.model.dto.requests.SubscriptionEntityDTO;
import com.model.entity.SubscriptionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubscriptionEntityToSubscriptionEntityDTO {
    SubscriptionEntityToSubscriptionEntityDTO instance = Mappers.getMapper(SubscriptionEntityToSubscriptionEntityDTO.class);
    @Mapping(target = "owner", ignore = true)
    SubscriptionEntityDTO convert(SubscriptionEntity subscriptionEntity);
}
