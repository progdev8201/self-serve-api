package com.repository;

import com.model.entity.Bill;
import com.model.entity.Guest;
import com.model.enums.BillStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findAllByBillStatusAndRestaurant_Id(BillStatus billStatus,Long restaurantId);
    List<Bill> findAllByDateBetweenAndBillStatusAndRestaurant_Id(LocalDate begin,LocalDate end,BillStatus billStatus,Long restaurantId);
}
