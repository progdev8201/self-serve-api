package com.repository;

import com.model.entity.CheckItem;
import com.model.entity.CustomProprety;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomPropretyRepository extends JpaRepository<CustomProprety, Long> {
}
