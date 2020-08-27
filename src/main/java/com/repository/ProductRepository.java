package com.repository;

import com.model.entity.Guest;
import com.model.entity.OrderItem;
import com.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);
}
