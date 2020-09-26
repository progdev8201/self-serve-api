package com.service;

import com.mapping.*;
import com.model.dto.*;
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

    public BillDTO makeOrder(ProductDTO productToAdd, String guestUsername, Long billId, Long restaurentTableId) {
        Bill bill = findBill(billId);
        List<OrderItem> orderItemList = findOrderItemsInDb(productToAdd, bill);
        Guest guest = guestRepository.findByUsername(guestUsername).get();
        //TODO:meilleur solution?? a voir mais il faut retrouver le restaurent pour l'associé au bill
        RestaurentTable restaurentTable = restaurentTableRepository.findById(restaurentTableId).get();
        Restaurant restaurant = orderItemList.get(0).getProduct().getMenu().getRestaurant();

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
         restaurant =restaurantRepository.save(restaurant);
        //TODO notify kitchen
        //set valeur retour
        BillDTO returnValue = dtoUtils.generateBillDTOWithOrderItems(restaurant.getBill().stream().filter(x -> x.getId().equals(bill.getId())).findFirst().get());

        return returnValue;
    }

    public BillDTO fetchBill (Long billId){
        Bill bill = billRepository.findById(billId).get();
        return dtoUtils.generateBillDTOWithOrderItems(bill);
    }


    private List<OrderItem> findOrderItemsInDb(ProductDTO productToAdd, Bill bill) {
        List<OrderItem> orderItems = new ArrayList<>();
        Product product = productRepository.findById(productToAdd.getId()).get();

        OrderItem orderItem = ProductToOrderItems.instance.convert(product);
        orderItem.setProduct(product);
        orderItem.setOrderStatus(ProgressStatus.PROGRESS);
        orderItem.setBill(bill);
        orderItem.setDelaiDePreparation(LocalDateTime.now().minusMinutes(product.getTempsDePreparation()));
        //orderItem = orderItemRepository.save(orderItem);
        orderItem.setOption(new ArrayList<>());
        for(OptionDTO optionDTO : productToAdd.getOptions()){
            Option option = OptionDTOToOption.instance.convert(optionDTO);
            option.setCheckItemList(new ArrayList<>());
            for(CheckItemDTO checkItemDTO :optionDTO.getCheckItemList()){
                CheckItem checkItem =CheckItemDTOCheckItem.instance.convert(checkItemDTO);
                option.getCheckItemList().add(checkItem);
            }
            orderItem.getOption().add(option);
        }
        orderItems.add(orderItem);
        orderItem = orderItemRepository.save(orderItem);
        product.setOrderItems(initEmptyList(product.getOrderItems()));
        product.getOrderItems().add(orderItem);
        productRepository.save(product);


        bill.setPrixTotal( bill.getPrixTotal()+orderItem.getPrix());


        return orderItems;
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
            bill =billRepository.save(bill);
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
