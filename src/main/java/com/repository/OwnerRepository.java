package com.repository;

import com.model.entity.OrderItem;
import com.model.entity.Owner;
import com.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByUsername(String username);

}
