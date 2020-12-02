package com.service.Util;

import com.mapping.*;
import com.model.dto.*;
import com.model.entity.*;
import com.repository.ImgFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DTOUtils {
    @Autowired
    private ImgFileRepository imgFileRepository;

    //BILL DTO UTILS

    public BillDTO mapBillToBillDTOWithOrderItems(Bill bill) {
        //map bill to bill dto
        BillDTO billDTO = BillToBillDTO.instance.convert(bill);

        billDTO.setOrderCustomer(GuestToGuestDTO.instance.convert(bill.getOrderCustomer()));

        billDTO.setOrderItems(bill.getOrderItems()
                .stream()
                .map(orderItem -> mapOrderItemToOrderItemDTOForBill(orderItem))
                .collect(Collectors.toList()));

        return billDTO;
    }

    private OrderItemDTO mapOrderItemToOrderItemDTOForBill(OrderItem orderItem) {
        OrderItemDTO orderItemDTO = OrderItemToOrderItemDTO.instance.convert(orderItem);

        ProductDTO productDTO = ProductToProductDTO.instance.convert(orderItem.getProduct());
        ImgFileDTO imgFileDTO = ImgFileToImgFileDTO.instance.convert(orderItem.getProduct().getImgFile());
        productDTO.setImgFileDTO(imgFileDTO);

        orderItemDTO.setProduct(productDTO);

        orderItemDTO.setOption(orderItem.getOption()
                .stream()
                .map(option -> mapOptionToOptionDTOForBill(option))
                .collect(Collectors.toList()));

        return orderItemDTO;
    }

    public MenuDTO mapMenuToMenuDTO(Menu menu) {
        MenuDTO menuDTO = MenuToMenuDTO.instance.convert(menu);
        menuDTO.setRestaurant(RestaurantToRestaurantDTO.instance.convert(menu.getRestaurant()));
        menuDTO.setProducts(menu.getProducts()
                .stream()
                .map(product -> mapProductToProductDTO(product))
                .collect(Collectors.toList()));
        return menuDTO;
    }

    private OptionDTO mapOptionToOptionDTOForBill(Option option) {
        OptionDTO optionDTO = OptionToOptionDTO.instance.convert(option);

        //map option to option dto
        optionDTO.setCheckItemList(option.getCheckItemList()
                .stream()
                .map(checkItem -> {
                    CheckItemDTO checkItemDTO = CheckItemToCheckItemDTO.instance.convert(checkItem);
                    checkItemDTO.setActive(checkItem.isActive());
                    return checkItemDTO;
                })
                .collect(Collectors.toList()));

        return optionDTO;
    }

    //PRODUCT DTO UTILS
    public static List<ProductDTO> mapProductListToProductDTOList(List<Product> products) {
        return products.stream()
                .map(product -> mapProductToProductDTO(product))
                .collect(Collectors.toList());
    }

    public static List<Product> mapProductListDTOToProductList(List<ProductDTO> productDTOS, ImgFileRepository imgFileRepository) {
        return productDTOS.stream()
                .map(productDTO -> mapProductDTOToProduct(productDTO, imgFileRepository))
                .collect(Collectors.toList());
    }

    public static ProductDTO mapProductToProductDTO(Product product) {
        ProductDTO productDTO = ProductToProductDTO.instance.convert(product);
        productDTO.setImgFileDTO(ImgFileToImgFileDTO.instance.convert(product.getImgFile()));
        productDTO.setMenuType(product.getMenuType());

        //map product checkItems
        productDTO.setCheckItems(product.getCheckItems()
                .stream()
                .map(CheckItemToCheckItemDTO.instance::convert)
                .collect(Collectors.toList()));

        //map product options
        productDTO.setOptions(product.getOptions()
                .stream()
                .map(option -> mapOptionToOptionDTO(option))
                .collect(Collectors.toList()));

        return productDTO;
    }

    public static Product mapProductDTOToProduct(ProductDTO productDTO, ImgFileRepository imgFileRepository) {
        Product product = ProductDTOToProduct.instance.convert(productDTO);

        if (productDTO.getImgFileDTO() != null)
            product.setImgFile(imgFileRepository.findById(productDTO.getImgFileDTO().getId()).get());

        product.setMenuType(productDTO.getMenuType());

        //map product checkItems
        product.setCheckItems(productDTO.getCheckItems()
                .stream()
                .map(CheckItemDTOCheckItemImpl.instance::convert)
                .collect(Collectors.toList()));

        //map product options
        product.setOptions(productDTO.getOptions()
                .stream()
                .map(optionDTO -> mapOptionDTOToOption(optionDTO))
                .collect(Collectors.toList()));

        return product;
    }

    private static OptionDTO mapOptionToOptionDTO(Option option) {
        OptionDTO optionDTO = OptionToOptionDTO.instance.convert(option);

        //map check item list to check item dto
        optionDTO.setCheckItemList(option.getCheckItemList()
                .stream()
                .map(CheckItemToCheckItemDTO.instance::convert)
                .collect(Collectors.toList()));

        return optionDTO;
    }

    private static Option mapOptionDTOToOption(OptionDTO optionDTO) {
        Option option = OptionDTOToOption.instance.convert(optionDTO);

        option.setCheckItemList(optionDTO.getCheckItemList()
                .stream()
                .map(CheckItemDTOCheckItem.instance::convert)
                .collect(Collectors.toList()));

        return option;
    }

    //RATE DTO UTILS

    public RateDTO generateRateDTO(Rate rate) {
        return RateToRateDTO.instance.convert(rate);
    }

    //RESTAURANT TABLE UTILS

    public RestaurentTableDTO mapRestaurantTableToRestaurantTableDTO(RestaurentTable restaurentTable) {

        RestaurentTableDTO restaurentTableDTO = RestaurentTableToRestaurenTableDTO.instance.convert(restaurentTable);
        restaurentTableDTO.setImgFileDTO(ImgFileToImgFileDTO.instance.convert(restaurentTable.getImgFile()));
        List<BillDTO> billDTOS = new ArrayList<>();
        if (Objects.nonNull(restaurentTable.getBills())) {
            restaurentTable.getBills().forEach(bill -> {
                BillDTO billDTO = mapBillToBillDTOWithOrderItems(bill);
                billDTOS.add(billDTO);
            });
        }
        restaurentTableDTO.setBills(billDTOS);
        return restaurentTableDTO;
    }

    // ORDER ITEM DTO UTILS

    public OrderItemDTO mapOrderItemToOrderItemDTO(OrderItem orderItem) {
        return OrderItemToOrderItemDTO.instance.convert(orderItem);
    }

    // RESTAURANT DTO UTILS

    public RestaurantDTO mapRestaurantToRestaurantDTO(Restaurant restaurant) {
        RestaurantDTO restaurantDTO = RestaurantToRestaurantDTO.instance.convert(restaurant);
        restaurantDTO.setImgFile(ImgFileToImgFileDTO.instance.convert(restaurant.getImgFile()));
        restaurantDTO.setRestaurentTables(new ArrayList<>());
        restaurantDTO.setRestaurentTables(restaurant.getRestaurentTables()
                .stream()
                .map(restaurentTable -> mapRestaurantTableToRestaurantTableDTO(restaurentTable))
                .collect(Collectors.toList()));

        return restaurantDTO;
    }

    public RestaurantSelectionDTO mapRestaurantToRestaurantSelectionDTO(Restaurant restaurant) {
        RestaurantSelectionDTO restaurantSelectionDTO = new RestaurantSelectionDTO();
        restaurantSelectionDTO.setRestaurantId(restaurant.getId());
        restaurantSelectionDTO.setRestaurantName(restaurant.getName());
        restaurantSelectionDTO.setRestaurentTablesDTO(restaurant.getRestaurentTables().stream()
                .map(restaurentTable -> RestaurentTableToRestaurenTableDTO.instance.convert(restaurentTable))
                .collect(Collectors.toList()));
        restaurantSelectionDTO.setMenuDTOS(restaurant.getMenus().stream()
                .map(menu -> mapMenuToMenuDTO(menu))
                .collect(Collectors.toList()));
        return restaurantSelectionDTO;
    }

}
