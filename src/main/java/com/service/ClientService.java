package com.service;

import com.mapping.BillToBillDTO;
import com.mapping.OrderItemDTOToOrderItem;
import com.model.dto.BillDTO;
import com.model.dto.OrderItemDTO;
import com.model.entity.*;
import com.model.enums.BillStatus;
import com.model.enums.ProgressStatus;
import com.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ClientService {
    BillRepository billRepository;
    GuestRepository guestRepository;
    MenuRepository menuRepository;
    OrderItemRepository orderItemRepository;

    @Autowired
    public ClientService(BillRepository billRepository, GuestRepository guestRepository, MenuRepository menuRepository, OrderItemRepository orderItemRepository) {
        this.billRepository = billRepository;
        this.guestRepository = guestRepository;
        this.menuRepository = menuRepository;
        this.orderItemRepository = orderItemRepository;
    }


    public BillRepository getBillRepository() {
        return billRepository;
    }

    public void setBillRepository(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public BillDTO makeOrder(List<OrderItemDTO> orderItemDTOList, String guestUsername, Long billId) {
        Bill bill = null;
        if(Objects.nonNull(billId)){
            bill = billRepository.findById(billId).get();
        }
        if (Objects.isNull(bill)) {
            bill = new Bill();
        }
        List<OrderItem> orderItemList = new ArrayList<>();
        for (OrderItemDTO orderItemDTO : orderItemDTOList) {
            OrderItem orderItem = orderItemRepository.save(OrderItemDTOToOrderItem.instance.convert(orderItemDTO));
            orderItem.setOrderStatus(ProgressStatus.PROGRESS);
            orderItemList.add(orderItem);
            bill.setPrixTotal(bill.getPrixTotal() + orderItem.getPrix());
        }
        Guest guest = guestRepository.findByUsername(guestUsername).get();

        ////meilleur solution?? a voir mais il faut retrouver le restaurent pour l'associé au bill
        Restaurant restaurant = orderItemList.get(0).getProduct().getMenu().getRestaurant();

        bill.setOrderCustomer(guest);
        bill.getOrderItems().addAll(bill.getOrderItems());
        bill.setOrderItems(bill.getOrderItems());
        bill.setRestaurant(restaurant);
        bill.setBillStatus(BillStatus.PROGRESS);

        //////notify kitchen
        return BillToBillDTO.instance.convert(billRepository.save(bill));
    }

    public boolean makePayment(Long billId) {

        Bill bill = billRepository.findById(billId).get();
        if (Objects.isNull(bill)) {
            bill.setBillStatus(BillStatus.PAYED);
            if (Objects.nonNull(billRepository.save(bill))) {
                return true;
            }
        }
        return false;
    }

}
