package com.service;

import com.model.entity.Bill;
import com.model.entity.OrderItem;
import com.model.entity.Restaurant;
import com.model.enums.BillStatus;
import com.model.enums.MenuType;
import com.model.enums.ProgressStatus;
import com.model.enums.RestaurantType;
import com.repository.BillRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
    @InjectMocks
    ClientService clientService;

    @Mock
    RestaurentTableService restaurentTableService;

    @Mock
    BillRepository billRepository;

    @Captor
    ArgumentCaptor<Bill> billArgumentCaptor;

    @Test
    public void makePaymentDineIn() {
        Mockito.when(billRepository.findById(any(Long.class))).thenReturn(Optional.of(initBill()));
        assertFalse(clientService.makePayment(1L));
        Mockito.verify(restaurentTableService,Mockito.times(0)).deleteBillFromTable(any(Bill.class));
    }
    @Test
    public void makePaymentDineInTerminalRequest() {
        Bill bill = initBill();
        bill.getOrderItems().get(0).setMenuType(MenuType.TERMINALREQUEST);
        Mockito.when(billRepository.findById(any(Long.class))).thenReturn(Optional.of(bill));
        Mockito.when(billRepository.save(any(Bill.class))).thenReturn(bill);
        assertTrue(clientService.makePayment(1L));
        Mockito.verify(restaurentTableService,Mockito.times(1)).deleteBillFromTable(any(Bill.class));
    }
    @Test
    public void makePaymentFastFoodAllOrderItemsCompleted() {
        Bill bill = initBill();
        bill.getRestaurant().setRestaurantType(RestaurantType.FASTFOOD);
        bill.setOrderItems(initOrderItemsCompleted());
        Mockito.when(billRepository.findById(any(Long.class))).thenReturn(Optional.of(bill));
        Mockito.when(billRepository.save(any(Bill.class))).thenReturn(bill);
        assertTrue(clientService.makePayment(1L));
        Mockito.verify(restaurentTableService,Mockito.times(1)).deleteBillFromTable(any(Bill.class));
    }
    @Test
    public void makePaymentFastFood() {
        Bill bill = initBill();
        bill.getRestaurant().setRestaurantType(RestaurantType.FASTFOOD);
        Mockito.when(billRepository.findById(any(Long.class))).thenReturn(Optional.of(bill));
        Mockito.when(billRepository.save(any(Bill.class))).thenReturn(bill);
        assertTrue(clientService.makePayment(1L));
        Mockito.verify(billRepository,Mockito.times(1)).save(any(Bill.class));
        Mockito.verify(billRepository).save(billArgumentCaptor.capture());
        assertEquals(BillStatus.PAYED,billArgumentCaptor.getValue().getBillStatus());
    }



    private Bill initBill() {
        Bill bill = new Bill();
        bill.setRestaurant(initRestaurant());
        bill.setOrderItems(initOrderItemsNotCompleted());
        return bill;
    }

    private Restaurant initRestaurant() {
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantType(RestaurantType.DINEIN);
        return restaurant;
    }

    private List<OrderItem> initOrderItemsNotCompleted() {
        List<OrderItem> orderItems = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderStatus(ProgressStatus.PROGRESS);
            orderItems.add(orderItem);
        }
        return orderItems;
    }
    private List<OrderItem> initOrderItemsCompleted() {
        List<OrderItem> orderItems = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderStatus(ProgressStatus.COMPLETED);
            orderItems.add(orderItem);
        }
        return orderItems;
    }
}