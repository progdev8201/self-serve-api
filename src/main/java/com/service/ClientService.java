package com.service;

import com.mapping.*;
import com.model.dto.BillDTO;
import com.model.dto.OrderItemDTO;
import com.model.entity.*;
import com.model.enums.BillStatus;
import com.model.enums.ProgressStatus;
import com.repository.*;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class ClientService {
    BillRepository billRepository;
    GuestRepository guestRepository;
    MenuRepository menuRepository;
    OrderItemRepository orderItemRepository;
    ProductRepository productRepository;
    RestaurantRepository restaurantRepository;

    RestaurentTableService restaurentTableService;

    RestaurentTableRepository restaurentTableRepository;

    @Autowired
    public ClientService(BillRepository billRepository, GuestRepository guestRepository, MenuRepository menuRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository, RestaurantRepository restaurantRepository, RestaurentTableService restaurentTableService, RestaurentTableRepository restaurentTableRepository) {
        this.billRepository = billRepository;
        this.guestRepository = guestRepository;
        this.menuRepository = menuRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.restaurantRepository = restaurantRepository;
        this.restaurentTableService = restaurentTableService;
        this.restaurentTableRepository = restaurentTableRepository;
    }

    public BillDTO makeOrder(List<OrderItemDTO> orderItemDTOList, String guestUsername, Long billId, Long restaurentTableId) {
        Bill bill = null;
        if (Objects.nonNull(billId)) {
            bill = billRepository.findById(billId).get();
        }
        if (Objects.isNull(bill)) {
            bill = new Bill();
        }
        List<OrderItem> orderItemList = new ArrayList<>();
        for (OrderItemDTO orderItemDTO : orderItemDTOList) {

            Product product = productRepository.findById(orderItemDTO.getProduct().getId()).get();
            OrderItem orderItem = OrderItemDTOToOrderItem.instance.convert(orderItemDTO);
            orderItem.setProduct(product);
            orderItem.setOrderStatus(ProgressStatus.PROGRESS);
            orderItem.setBill(bill);
            orderItem.setDelaiDePreparation(LocalDateTime.now().minusMinutes(product.getTempsDePreparation()));
            orderItem = orderItemRepository.save(orderItem);
            orderItemList.add(orderItem);
            bill.setPrixTotal(bill.getPrixTotal() + orderItem.getPrix());
        }
        Guest guest = guestRepository.findByUsername(guestUsername).get();

        ////meilleur solution?? a voir mais il faut retrouver le restaurent pour l'associé au bill
        Restaurant restaurant = orderItemList.get(0).getProduct().getMenu().getRestaurant();
        RestaurentTable restaurentTable = restaurentTableRepository.findById(restaurentTableId).get();
        bill.setOrderCustomer(guest);
        if (Objects.isNull(bill.getOrderItems())) {
            bill.setOrderItems(new ArrayList<>());
        }
        bill.getOrderItems().addAll(orderItemList);
        bill.setOrderItems(bill.getOrderItems());
        bill.setRestaurant(restaurant);
        bill.setBillStatus(BillStatus.PROGRESS);
        if (Objects.isNull(restaurentTable.getBill())) {
            restaurentTable.setBill(new ArrayList<>());
        }
        if (!restaurentTable.getBill().stream().anyMatch(x -> x.getId().equals(billId))) {
            bill.setRestaurentTable(restaurentTable);
            restaurentTable.getBill().add(bill);
            System.out.println("yiooooo/*************************************************/");
        }
        bill = billRepository.save(bill);
        if (Objects.isNull(restaurant.getBill())) {
            restaurant.setBill(new ArrayList<>());
        }


        //////notify kitchen
        BillDTO returnValue = BillToBillDTO.instance.convert(bill);
        List<OrderItemDTO> returnBillOrderItems = new ArrayList<>();
        for (OrderItem orderItem : bill.getOrderItems()) {
            OrderItemDTO orderItemDTO = OrderItemToOrderItemDTO.instance.convert(orderItem);
            orderItemDTO.setProduct(ProductToProductDTO.instance.convert(orderItem.getProduct()));
            returnBillOrderItems.add(orderItemDTO);
        }
        returnValue.setOrderCustomer(GuestToGuestDTO.instance.convert(bill.getOrderCustomer()));
        returnValue.setOrderItems(returnBillOrderItems);

        return returnValue;
    }

    public boolean makePayment(Long billId) {

        Bill bill = billRepository.findById(billId).get();
        if (Objects.nonNull(bill)) {
            bill.setBillStatus(BillStatus.PAYED);
            restaurentTableService.deleteBillFromTable(bill);
            bill.setRestaurentTable(null);
            if (Objects.nonNull(billRepository.save(bill))) {
                return true;
            }
        }
        return false;
    }

}
