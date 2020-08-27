package com.repository;

import com.model.entity.Bill;
import com.model.entity.CheckItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckItemRepository extends JpaRepository<CheckItem, Long> {
}
