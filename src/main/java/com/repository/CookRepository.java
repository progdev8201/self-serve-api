package com.repository;

import com.model.entity.Cook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CookRepository extends JpaRepository<Cook,Long> {
    Optional<Cook> findCookByRestaurant_Id(Long id);
    Optional<Cook> findCookByUsername(String username);
}
