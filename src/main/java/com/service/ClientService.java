package com.service;

import com.model.entity.*;
import com.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

public class ClientService {
    BillRepository billRepository;
    GuestRepository guestRepository;
    MenuRepository menuRepository;

    @Autowired
    public ClientService(BillRepository billRepository, GuestRepository guestRepository, MenuRepository menuRepository) {
        this.billRepository = billRepository;
        this.guestRepository = guestRepository;
        this.menuRepository = menuRepository;
    }

    public BillRepository getBillRepository() {
        return billRepository;
    }

    public void setBillRepository(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public Bill makeOrder(List<OrderItem> orderItemList, String guestUsername,Long billId) {
        Bill bill =billRepository.findById(billId).get();
        if(Objects.isNull(bill)){
            //si bill status ==progress
            bill = new Bill();
            for (OrderItem orderItem : orderItemList) {
                orderItem.setOrderStatus(ProgressStatus.PROGRESS);
                bill.setPrixTotal(bill.getPrixTotal() + orderItem.getPrix());
            }
            Guest guest = guestRepository.findByUsername(guestUsername).get();

            ////meilleur solution?? a voir mais il faut retrouver le restaurent pour l'associé au bill
            Restaurant restaurant =orderItemList.get(0).getProduct().getMenu().getRestaurant();

            bill.setOrderCustomer(guest);
            bill.getOrderItems().addAll(bill.getOrderItems());
            bill.setOrderItems( bill.getOrderItems());
            bill.setRestaurant(restaurant);
            bill.setBillStatus(BillStatus.PROGRESS);
            return billRepository.save(bill);
        }
        //////notify kitchen
        return null;
    }
    public boolean makePayment(Long billId){

        Bill bill =billRepository.findById(billId).get();
        if(Objects.isNull(bill)){
            bill.setBillStatus(BillStatus.PAYED);
            if(Objects.nonNull(billRepository.save(bill))){
                return true;
            }
        }
        return false;
    }

}
