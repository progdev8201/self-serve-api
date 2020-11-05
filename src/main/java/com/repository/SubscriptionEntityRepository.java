package com.repository;

import com.model.entity.Bill;
import com.model.entity.Guest;
import com.model.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionEntityRepository extends JpaRepository<SubscriptionEntity, Long> {
    Optional<SubscriptionEntity> findBySubscriptionId(String subscriptionId);
}
