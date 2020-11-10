package com.repository;

import com.model.entity.Owner;
import com.model.entity.StripeSubscriptionProducts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StripeSubscriptionProductRepository extends JpaRepository<StripeSubscriptionProducts, Long> {
    Optional<StripeSubscriptionProducts> findByPriceId(String priceId);

}
