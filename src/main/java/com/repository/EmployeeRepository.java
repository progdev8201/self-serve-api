package com.repository;

import com.model.entity.Cook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Cook, Long> {
}
