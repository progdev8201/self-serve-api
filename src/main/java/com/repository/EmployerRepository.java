package com.repository;

import com.model.entity.Employer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployerRepository extends JpaRepository<Employer,Long> {
    Optional<Employer> findEmployerByUsernameAndRole(String username,String role);

    Optional<Employer> findEmployerByUsername(String username);

    List<Employer> findAllByRestaurant_Id(Long id);

    boolean existsByUsername(String username);
}
