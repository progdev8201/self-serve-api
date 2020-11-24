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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class ClientService {
    private BillRepository billRepository;
    private GuestRepository guestRepository;
    private MenuRepository menuRepository;
    private OrderItemRepository orderItemRepository;
    private ProductRepository productRepository;
    private RestaurantRepository restaurantRepository;

    private RestaurentTableService restaurentTableService;

    private RestaurentTableRepository restaurentTableRepository;

    DTOUtils dtoUtils;
    private static final int DOUBLE_SCALE_PLACES = 2;

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

    public BillDTO initBill() {
        Bill bill = new Bill();
        billRepository.save(bill);
        return BillToBillDTO.instance.convert(bill);
    }

    public BillDTO makeOrder(ProductDTO productToAdd, String guestUsername, Long billId, Long restaurentTableId, String commentaire) {
        List<OrderItem> orderItemList = initOrderItems(productToAdd,commentaire);
        Guest guest = guestRepository.findByUsername(guestUsername).get();

        //TODO:meilleur solution?? a voir mais il faut retrouver le restaurent pour l'associé au bill
        RestaurentTable restaurentTable = restaurentTableRepository.findById(restaurentTableId).get();

        Restaurant restaurant = restaurentTable.getRestaurant();
        Bill bill = initBill(billId, orderItemList, guest, restaurant);

        AddBillToTable(billId, restaurentTable, bill);
        addBillToRestaurant(billId, restaurant, bill);
        setOrderItemsBill(orderItemList, bill);
        restaurant = restaurantRepository.save(restaurant);
        //TODO notify kitchen
        //set valeur retour
        BillDTO returnValue = dtoUtils.mapBillToBillDTOWithOrderItems(findBillInRestaurantList(restaurant, bill));

        return returnValue;
    }

    private void AddBillToTable(Long billId, RestaurentTable restaurentTable, Bill bill) {
        if (!isBillInStream(restaurentTable.getBills().stream(), billId)) {
            restaurentTable.getBills().add(bill);
            bill.setRestaurentTable(restaurentTable);
        }
    }

    private void addBillToRestaurant(Long billId, Restaurant restaurant, Bill bill) {
        restaurant.setBill(initEmptyList(restaurant.getBill()));
        if (!isBillInStream(restaurant.getBill().stream(), billId)) {
            if (Objects.isNull(restaurant.getBill())) {
                restaurant.setBill(new ArrayList<>());
            }
            restaurant.getBill().add(bill);
        }
    }

    private void setOrderItemsBill(List<OrderItem> orderItemList, Bill bill) {
        orderItemList.forEach(orderItem -> {
            orderItem.setBill(bill);
        });
    }

    private Bill findBillInRestaurantList(Restaurant restaurant, Bill bill) {
        return restaurant.getBill().stream().filter(x -> x.getId().equals(bill.getId())).findFirst().get();
    }

    private Bill initBill(Long billId, List<OrderItem> orderItemList, Guest guest, Restaurant restaurant) {
        Bill bill = findBill(billId);

        bill.setOrderCustomer(guest);
        bill.setOrderItems(initEmptyList(bill.getOrderItems()));
        bill.setPrixTotal(bill.getPrixTotal()+calculerPriceBill(orderItemList));
        bill.getOrderItems().addAll(orderItemList);

        if (Objects.isNull(bill.getRestaurant())) {
            bill.setRestaurant(restaurant);
        }
        bill.setBillStatus(BillStatus.PROGRESS);
        return bill;
    }

    public BillDTO fetchBill(Long billId) {
        Bill bill = billRepository.findById(billId).get();
        return dtoUtils.mapBillToBillDTOWithOrderItems(bill);
    }


    private List<OrderItem> initOrderItems(ProductDTO productToAdd, String commentaire) {
        List<OrderItem> orderItems = new ArrayList<>();
        /**aller creer order item en fonction du produit**/
        Product product = productRepository.findById(productToAdd.getId()).get();
        OrderItem orderItem = createOrderItemFromProduct(productToAdd,  commentaire, product);

        addCheckItemPrice(orderItem, orderItem.getCheckItems());
        setOrderItemOptions(productToAdd, orderItem);

        orderItems.add(orderItem);
        product.setOrderItems(initEmptyList(product.getOrderItems()));
        product.getOrderItems().addAll(orderItems);

        productRepository.save(product);
        return orderItems;
    }

    private double calculerPriceBill(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItem::getPrix)
                .collect(Collectors.summarizingDouble(Double::doubleValue))
                .getSum();
    }

    private OrderItem createOrderItemFromProduct(ProductDTO productToAdd,  String commentaire, Product product) {
        OrderItem orderItem = ProductToOrderItems.instance.convert(product);
        orderItem.setProduct(product);
        orderItem.setOrderStatus(ProgressStatus.PROGRESS);

        Date dateCommandeFini = setDatePreparation(product.getTempsDePreparation());

        orderItem.setProduct(product);
        orderItem.setTempsDePreparation(dateCommandeFini);
        orderItem.setDelaiDePreparation(LocalDateTime.now().minusMinutes(product.getTempsDePreparation()));
        orderItem.setCommentaires(commentaire);
        orderItem.setOption(new ArrayList<>());
        orderItem.setCheckItems(setUpCheckItems(productToAdd.getCheckItems()));
        return orderItem;
    }

    private Date setDatePreparation(int tempsPrepatarion) {
        Date dateCommandeFini = new Date(System.currentTimeMillis() + (tempsPrepatarion) * 60000);
        return dateCommandeFini;
    }

    private void setOrderItemOptions(ProductDTO productToAdd, OrderItem orderItem) {
        for (OptionDTO optionDTO : productToAdd.getOptions()) {
            Option option = OptionDTOToOption.instance.convert(optionDTO);
            option.setCheckItemList(setUpCheckItems(optionDTO.getCheckItemList()));
            addCheckItemPrice(orderItem, option.getCheckItemList());
            orderItem.getOption().add(option);
        }
    }

    private List<CheckItem> setUpCheckItems(List<CheckItemDTO> checkItemDTOS) {
        List<CheckItem> checkItemList = new ArrayList<>();
        checkItemDTOS.forEach(checkItemDTO -> {
            CheckItem checkItem = CheckItemDTOCheckItem.instance.convert(checkItemDTO);
            checkItemList.add(checkItem);
        });
        return checkItemList;
    }

    private void addCheckItemPrice(OrderItem orderItem, List<CheckItem> checkItems) {
        checkItems.forEach(checkItem -> {
            if (checkItem.isActive()) {
                orderItem.setPrix(roundDouble(orderItem.getPrix() + checkItem.getPrix()));
            }
        });
    }

    private Boolean isBillInStream(Stream stream, Long billId) {
        return stream.anyMatch(x -> ((Bill) x).getId().equals(billId));
    }

    private Bill findBill(Long billId) {
        Bill bill = null;
        if (Objects.nonNull(billId)) {
            bill = billRepository.findById(billId).get();
        } else if (Objects.isNull(bill)) {
            bill = new Bill();
            bill = billRepository.save(bill);
        }
        return bill;
    }

    private List initEmptyList(List list) {
        if (Objects.isNull(list)) {
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

    private static double roundDouble(double prix) {

        BigDecimal bigDecimal = new BigDecimal(Double.toString(prix));
        bigDecimal = bigDecimal.setScale(DOUBLE_SCALE_PLACES, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

}
