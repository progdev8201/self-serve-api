package com.repository;

import com.model.entity.OrderItem;
import com.model.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RateRepository extends JpaRepository<Rate, Long> {
}
