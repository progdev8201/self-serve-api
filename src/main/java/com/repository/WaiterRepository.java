package com.repository;

import com.model.entity.Waiter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WaiterRepository extends JpaRepository<Waiter,Long> {
    Optional<Waiter> findWaiterByRestaurant_Id(Long id);
    Optional<Waiter> findWaiterByUsername(String username);
}
