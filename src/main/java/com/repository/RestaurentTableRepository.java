package com.repository;

import com.model.entity.Bill;
import com.model.entity.RestaurentTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurentTableRepository extends JpaRepository<RestaurentTable, Long> {
}
