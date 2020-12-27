package com.repository;

import com.model.entity.Bill;
import com.model.entity.Guest;
import com.model.enums.BillStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findAllByDateBetweenAndBillStatusAndRestaurant_Id(LocalDateTime begin, LocalDateTime end, BillStatus billStatus, Long restaurantId);
}
