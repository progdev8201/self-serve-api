package com.service;

import com.model.entity.Bill;
import com.model.entity.RestaurentTable;
import com.repository.RestaurentTableRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class RestaurentTableServiceTest {
    @InjectMocks
    RestaurentTableService restaurentTableService;

    @Mock
    RestaurentTableRepository restaurentTableRepository;

    @Test
    public void removeBillFromDatabase(){
        Bill bill = initBill();
        Mockito.when(restaurentTableRepository.save(any(RestaurentTable.class))).thenReturn(bill.getRestaurentTable());
        RestaurentTable restaurentTable =restaurentTableService.deleteBillFromTable(bill);
        assertEquals(0,restaurentTable.getBills().size());
    }

    private Bill initBill(){
        Bill bill = new Bill();
        bill.setRestaurentTable(initRestaurentTable(bill));
        return bill;
    }

    private RestaurentTable initRestaurentTable(Bill bill){
        RestaurentTable restaurentTable = new RestaurentTable();
        List<Bill> bills = new ArrayList<>();
        bills.add(bill);
        restaurentTable.setBills(bills);
        return restaurentTable;
    }


}