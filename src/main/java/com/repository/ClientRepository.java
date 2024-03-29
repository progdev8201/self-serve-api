package com.repository;

import com.model.entity.Bill;
import com.model.entity.Client;
import com.model.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
