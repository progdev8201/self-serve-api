package com.service.DtoUtil;

import com.mapping.*;
import com.model.dto.*;
import com.model.entity.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DTOUtils {

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

    //MENU DTO UTILS

    public MenuDTO generateMenuDTO(Menu menu) {
        MenuDTO returnValue = MenuToMenuDTO.instance.convert(menu);
        returnValue.setSpeciaux(new ArrayList<>());
        for (Product special : menu.getSpeciaux()) {
            returnValue.getSpeciaux().add(ProductToProductDTO.instance.convert(special));
            returnValue.setSpeciaux(returnValue.getSpeciaux());
        }
        return returnValue;
    }

    //PRODUCT DTO UTILS

    public List<ProductDTO> generateProductDTOList(List<Product> products) {
        //create empty list
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> mapProductToProductDTO(product))
                .collect(Collectors.toList());

        return productDTOS;
    }

    public ProductDTO mapProductToProductDTO(Product product) {
        ProductDTO productDTO = ProductToProductDTO.instance.convert(product);
        productDTO.setImgFileDTO(ImgFileToImgFileDTO.instance.convert(product.getImgFile()));
        productDTO.setProductType(product.getProductType());

        //map product options
        productDTO.setOptions(product.getOptions()
                .stream()
                .map(option -> mapOptionToOptionDTO(option))
                .collect(Collectors.toList()));

        return productDTO;
    }

    private OptionDTO mapOptionToOptionDTO(Option option) {
        OptionDTO optionDTO = OptionToOptionDTO.instance.convert(option);

        //map check item list to check item dto
        optionDTO.setCheckItemList(option.getCheckItemList()
                .stream()
                .map(checkItem -> CheckItemToCheckItemDTO.instance.convert(checkItem))
                .collect(Collectors.toList()));

        return optionDTO;
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

        restaurantDTO.setRestaurentTables(restaurant.getRestaurentTables()
                .stream()
                .map(restaurentTable -> mapRestaurantTableToRestaurantTableDTO(restaurentTable))
                .collect(Collectors.toList()));

        return restaurantDTO;
    }

}
