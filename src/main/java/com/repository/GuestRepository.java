package com.repository;

import com.model.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    Optional<Guest> findByUsername(String username);
    boolean existsByUsername(String username);
}
