package com.service;

import com.mapping.*;
import com.model.dto.BillDTO;
import com.model.dto.OrderItemDTO;
import com.model.entity.*;
import com.model.enums.BillStatus;
import com.model.enums.ProgressStatus;
import com.repository.*;
import com.service.DtoUtil.DTOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

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

    DTOUtils dtoUtils;

    @Autowired
    public ClientService(BillRepository billRepository, GuestRepository guestRepository, MenuRepository menuRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository, RestaurantRepository restaurantRepository, RestaurentTableService restaurentTableService, RestaurentTableRepository restaurentTableRepository, DTOUtils dtoUtils) {
        this.billRepository = billRepository;
        this.guestRepository = guestRepository;
        this.menuRepository = menuRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.restaurantRepository = restaurantRepository;
        this.restaurentTableService = restaurentTableService;
        this.restaurentTableRepository = restaurentTableRepository;
        this.dtoUtils = dtoUtils;
    }

    public BillDTO makeOrder(List<OrderItemDTO> orderItemDTOList, String guestUsername, Long billId, Long restaurentTableId) {
        Bill bill = findBill(billId);
        List<OrderItem> orderItemList = findOrderItemsInDb(orderItemDTOList, bill);
        Guest guest = guestRepository.findByUsername(guestUsername).get();

        //TODO:meilleur solution?? a voir mais il faut retrouver le restaurent pour l'associé au bill
        Restaurant restaurant = orderItemList.get(0).getProduct().getMenu().getRestaurant();
        RestaurentTable restaurentTable = restaurentTableRepository.findById(restaurentTableId).get();

        bill.setOrderCustomer(guest);
        //
        bill.setOrderItems(initEmptyList(bill.getOrderItems()));
        bill.getOrderItems().addAll(orderItemList);
        if(Objects.isNull(bill.getRestaurant())){
            bill.setRestaurant(restaurant);
        }
        bill.setBillStatus(BillStatus.PROGRESS);
        restaurant.setBill(initEmptyList(restaurant.getBill()));
        if (!isBillInStream(restaurentTable.getBills().stream(),billId)) {
            bill.setRestaurentTable(restaurentTable);
            restaurentTable.getBills().add(bill);
        }
        if (!isBillInStream(restaurant.getBill().stream(),billId)) {
            if(Objects.isNull(restaurant.getBill())) {
                restaurant.setBill(new ArrayList<>());
            }
            restaurant.getBill().add(bill);
        }
         restaurantRepository.save(restaurant);
        //TODO notify kitchen
        //set valeur retour
        BillDTO returnValue = dtoUtils.constructBillDTOWithOrderItems(bill);

        return returnValue;
    }


    private List<OrderItem> findOrderItemsInDb(List<OrderItemDTO> orderItemDTOList, Bill bill) {
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
        return orderItemList;
    }
    private Boolean isBillInStream(Stream stream,Long billId){
       return stream.anyMatch(x -> ((Bill)x).getId().equals(billId));
    }
    private Bill findBill(Long billId) {
        Bill bill = null;
        if (Objects.nonNull(billId)) {
            bill = billRepository.findById(billId).get();
        }
        if (Objects.isNull(bill)) {
            bill = new Bill();
        }
        return bill;
    }
    private  List initEmptyList(List list){
        if(Objects.isNull(list)){
            list = new ArrayList();
        }
        return list;
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
