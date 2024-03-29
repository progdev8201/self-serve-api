package com.service;

import com.mapping.BillToBillDTO;
import com.mapping.CheckItemDTOCheckItem;
import com.mapping.OptionDTOToOption;
import com.mapping.ProductToOrderItems;
import com.model.dto.BillDTO;
import com.model.dto.CheckItemDTO;
import com.model.dto.OptionDTO;
import com.model.dto.ProductDTO;
import com.model.dto.requests.FindBillBetweenDateRequestDTO;
import com.model.entity.*;
import com.model.enums.BillStatus;
import com.model.enums.MenuType;
import com.model.enums.ProgressStatus;
import com.repository.*;
import com.service.Util.DTOUtils;
import com.service.validator.RestaurantOwnerShipValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
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
    private ProductRepository productRepository;
    private RestaurantRepository restaurantRepository;
    private RestaurentTableService restaurentTableService;
    private RestaurentTableRepository restaurentTableRepository;
    private RestaurantOwnerShipValidator restaurantOwnerShipValidator;
    private DTOUtils dtoUtils;
    private static final int DOUBLE_SCALE_PLACES = 2;

    @Autowired
    public ClientService(BillRepository billRepository, GuestRepository guestRepository, ProductRepository productRepository, RestaurantRepository restaurantRepository, RestaurentTableService restaurentTableService, RestaurentTableRepository restaurentTableRepository, DTOUtils dtoUtils,RestaurantOwnerShipValidator restaurantOwnerShipValidator) {
        this.billRepository = billRepository;
        this.guestRepository = guestRepository;
        this.productRepository = productRepository;
        this.restaurantRepository = restaurantRepository;
        this.restaurentTableService = restaurentTableService;
        this.restaurentTableRepository = restaurentTableRepository;
        this.dtoUtils = dtoUtils;
        this.restaurantOwnerShipValidator = restaurantOwnerShipValidator;
    }

    //PUBLIC METHODS

    public BillDTO initBill() {
        Bill bill = new Bill();
        bill.setPrix(BigDecimal.valueOf(0));
        return BillToBillDTO.instance.convert(billRepository.save(bill));
    }

    public BillStatus findBillStatus(Long id) {
        return billRepository.findById(id).get().getBillStatus();
    }

    public boolean makePayment(Long billId) {
        Bill bill = billRepository.findById(billId).get();
        if (Objects.nonNull(bill)) {
            bill.setBillStatus(BillStatus.PAYED);
            unlinkBillAndTable(bill);
            if (Objects.nonNull(billRepository.save(bill))) {
                return true;
            }
        }
        return false;
    }

    public BillDTO fetchBill(Long billId) {
        Bill bill = billRepository.findById(billId).get();
        return dtoUtils.mapBillToBillDTOWithOrderItems(bill);
    }

    /*On update juste le bill status pck cest juste ca on a besoin live*/
    public BillDTO updateBill(BillDTO billDTO) {
        Bill bill = billRepository.findById(billDTO.getId()).get();
        bill.setBillStatus(billDTO.getBillStatus());
        bill.setTips(billDTO.getTips().setScale(2,RoundingMode.UP));
        bill.setPrixTotal(bill.getPrix().add(bill.getTips()).setScale(2,RoundingMode.UP));
        return dtoUtils.mapBillToBillDTOWithOrderItems(billRepository.save(bill));
    }

    public BillDTO makeOrder(ProductDTO productToAdd, String guestUsername, Long billId, Long restaurentTableId, String commentaire) {
        //init orders
        List<OrderItem> orderItemList = initOrderItems(productToAdd, commentaire);

        //init bill
        Bill bill = initBill(billId, orderItemList, guestUsername, restaurentTableId);

        //set valeur retour

        return dtoUtils.mapBillToBillDTOWithOrderItems(bill);
    }

    public List<BillDTO> findAllPaidBillsByRestaurantBetweenDates(FindBillBetweenDateRequestDTO request) throws Exception {
        if (!restaurantOwnerShipValidator.hasOwnerRight(request.getRestaurantId()))
            throw new Exception();

        return billRepository.findAllByDateBetweenAndBillStatusAndRestaurant_Id(request.getBegin(), request.getEnd(), BillStatus.PAYED, request.getRestaurantId())
                .stream()
                .map(dtoUtils::mapBillToBillDTOWithOrderItems)
                .collect(Collectors.toList());
    }

    // PRIVATE METHODS

    private void addBillToValues(Long restaurantTableId, Bill bill, List<OrderItem> orderItemList) {
        //find restaurant table
        RestaurentTable restaurentTable = restaurentTableRepository.findById(restaurantTableId).get();

        // find restaurant
        Restaurant restaurant = restaurentTable.getRestaurant();

        addBillToValues(orderItemList, restaurentTable, restaurant, bill);
    }

    private void addBillToValues(List<OrderItem> orderItemList, RestaurentTable restaurentTable, Restaurant restaurant, Bill bill) {
        linkBillAndTable(bill.getId(), restaurentTable, bill);
        linkBillAndOrderItems(orderItemList, bill);
        linkBillAndRestaurant(bill.getId(), restaurant, bill);
    }

    private void linkBillAndTable(Long billId, RestaurentTable restaurentTable, Bill bill) {
        if (!isBillInStream(restaurentTable.getBills().stream(), billId)) {
            restaurentTable.getBills().add(bill);
            bill.setRestaurentTable(restaurentTable);
        }
    }

    private void linkBillAndRestaurant(Long billId, Restaurant restaurant, Bill bill) {
        restaurant.setBill(initEmptyList(restaurant.getBill()));

        //add empty bill to restaurant
        if (!isBillInStream(restaurant.getBill().stream(), billId)) {
            if (Objects.isNull(restaurant.getBill())) {
                restaurant.setBill(new ArrayList<>());
            }
            restaurant.getBill().add(bill);
        }
        if (Objects.isNull(bill.getRestaurant())) {
            bill.setRestaurant(restaurant);
        }
        //save le tout
        restaurantRepository.save(restaurant);

    }

    private void linkBillAndOrderItems(List<OrderItem> orderItemList, Bill bill) {
        orderItemList.forEach(orderItem -> {
            orderItem.setBill(bill);
        });
        bill.getOrderItems().addAll(orderItemList);
    }


    private Bill initBill(Long billId, List<OrderItem> orderItemList, String guestUsername, Long restaurantTableId) {
        Bill bill = findBill(billId);
        Guest guest = guestRepository.findByUsername(guestUsername).get();

        bill.setOrderCustomer(guest);
        bill.setOrderItems(initEmptyList(bill.getOrderItems()));
        bill.setPrix(bill.getPrix().add( calculerPriceBill(orderItemList)).setScale(2,RoundingMode.UP));
        bill.setTips(calculerTipsDeBase(bill.getPrix()).setScale(2,RoundingMode.UP));
        bill.setPrixTotal(bill.getPrix().add(bill.getTips()).setScale(2,RoundingMode.UP));
        bill.setBillStatus(BillStatus.PROGRESS);
        bill.setDate(LocalDateTime.now());

        addBillToValues(restaurantTableId, bill, orderItemList);
        return bill;
    }

    private List<OrderItem> initOrderItems(ProductDTO productToAdd, String commentaire) {
        List<OrderItem> orderItems = new ArrayList<>();
        /**aller creer order item en fonction du produit**/
        Product product = productRepository.findById(productToAdd.getId()).get();
        OrderItem orderItem = initOrderItem(productToAdd, commentaire, product);

        linkOrderItemAndProduct(orderItems, product, orderItem);

        return orderItems;
    }

    private BigDecimal calculerTipsDeBase(BigDecimal prix) {
        return BigDecimal.valueOf((15 * prix.doubleValue())/100);
    }

    private OrderItem initOrderItem(ProductDTO productToAdd, String commentaire, Product product) {
        OrderItem orderItem = createOrderItemFromProduct(productToAdd, commentaire, product);
        addCheckItemToOrderItemPrice(orderItem, orderItem.getCheckItems());
        setOrderItemOptions(productToAdd, orderItem);
        return orderItem;
    }

    private void linkOrderItemAndProduct(List<OrderItem> orderItems, Product product, OrderItem orderItem) {
        orderItems.add(orderItem);
        product.setOrderItems(initEmptyList(product.getOrderItems()));
        product.getOrderItems().addAll(orderItems);
    }

    private BigDecimal calculerPriceBill(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItem::getPrix)
                .reduce(BigDecimal.valueOf(0),BigDecimal::add);
    }

    private OrderItem createOrderItemFromProduct(ProductDTO productToAdd, String commentaire, Product product) {
        OrderItem orderItem = ProductToOrderItems.instance.convert(product);
        orderItem.setProduct(product);
        setProgressStatus(product, orderItem);

        Date dateCommandeFini = setDatePreparation(product.getTempsDePreparation());

        orderItem.setProduct(product);
        orderItem.setTempsDePreparation(dateCommandeFini);
        orderItem.setDelaiDePreparation(LocalDateTime.now().minusMinutes(product.getTempsDePreparation()));
        orderItem.setCommentaires(commentaire);
        orderItem.setOption(new ArrayList<>());
        orderItem.setCheckItems(setUpCheckItems(productToAdd.getCheckItems()));
        return orderItem;
    }

    private void setProgressStatus(Product product, OrderItem orderItem) {
        orderItem.setOrderStatus(ProgressStatus.PROGRESS);
        if (product.getMenuType() == MenuType.WAITERREQUEST || product.getMenuType() == MenuType.WAITERCALL || product.getMenuType() == MenuType.TERMINALREQUEST) {
            orderItem.setOrderStatus(ProgressStatus.READY);
        }
    }

    private Date setDatePreparation(int tempsPrepatarion) {
        Date dateCommandeFini = new Date(System.currentTimeMillis() + (tempsPrepatarion) * 60000);
        return dateCommandeFini;
    }

    private void setOrderItemOptions(ProductDTO productToAdd, OrderItem orderItem) {
        for (OptionDTO optionDTO : productToAdd.getOptions()) {
            Option option = OptionDTOToOption.instance.convert(optionDTO);
            option.setId(null);
            option.setCheckItemList(setUpCheckItems(optionDTO.getCheckItemList()));
            addCheckItemToOrderItemPrice(orderItem, option.getCheckItemList());
            orderItem.getOption().add(option);
        }
    }

    private List<CheckItem> setUpCheckItems(List<CheckItemDTO> checkItemDTOS) {
        List<CheckItem> checkItemList = new ArrayList<>();
        checkItemDTOS.forEach(checkItemDTO -> {
            CheckItem checkItem = CheckItemDTOCheckItem.instance.convert(checkItemDTO);
            checkItem.setId(null);
            if(Objects.isNull(checkItem.getPrix())){
                checkItem.setPrix(BigDecimal.valueOf(0));
            }
            checkItemList.add(checkItem);
        });
        return checkItemList;
    }

    private void addCheckItemToOrderItemPrice(OrderItem orderItem, List<CheckItem> checkItems) {
        checkItems.forEach(checkItem -> {
            if (checkItem.isActive()) {
                orderItem.setPrix(orderItem.getPrix().add(checkItem.getPrix()));
            }
        });
    }

    private Boolean isBillInStream(Stream stream, Long billId) {
        return stream.anyMatch(x -> ((Bill) x).getId().equals(billId));
    }

    private Bill findBill(Long billId) {
        if (Objects.nonNull(billId)) {
            return billRepository.findById(billId).get();
        } else {
            Bill bill = new Bill();
            bill.setPrix(BigDecimal.valueOf(0));
            return billRepository.save(bill);
        }
    }

    private List initEmptyList(List list) {
        if (Objects.isNull(list)) {
            list = new ArrayList();
        }
        return list;
    }


    private void unlinkBillAndTable(Bill bill) {
        restaurentTableService.deleteBillFromTable(bill);
        bill.setRestaurentTable(null);
    }

    private static double roundDouble(double prix) {

        BigDecimal bigDecimal = new BigDecimal(Double.toString(prix));
        bigDecimal = bigDecimal.setScale(DOUBLE_SCALE_PLACES, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

}
