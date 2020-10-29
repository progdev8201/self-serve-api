package com.service.DtoUtil;

import com.mapping.*;
import com.model.dto.*;
import com.model.entity.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DTOUtils {

    //BILL DTO UTILS

    public BillDTO generateBillDTOWithOrderItems(Bill bill) {
        BillDTO returnValue = BillToBillDTO.instance.convert(bill);
        List<OrderItemDTO> returnBillOrderItems = new ArrayList<>();
        for (OrderItem orderItem : bill.getOrderItems()) {
            OrderItemDTO orderItemDTO = OrderItemToOrderItemDTO.instance.convert(orderItem);
            ProductDTO productDTO = ProductToProductDTO.instance.convert(orderItem.getProduct());
            ImgFileDTO imgFileDTO = ImgFileToImgFileDTO.instance.convert(orderItem.getProduct().getImgFile());
            productDTO.setImgFileDTO(imgFileDTO);
            orderItemDTO.setProduct(productDTO);
            orderItemDTO.setOption(new ArrayList<>());
            for (Option option : orderItem.getOption()) {
                OptionDTO optionDTO = OptionToOptionDTO.instance.convert(option);
                optionDTO.setCheckItemList(new ArrayList<>());
                for (CheckItem checkItem : option.getCheckItemList()) {
                    CheckItemDTO checkItemDTO = CheckItemToCheckItemDTO.instance.convert(checkItem);
                    checkItemDTO.setActive(checkItem.isActive());
                    optionDTO.getCheckItemList().add(checkItemDTO);
                }
                orderItemDTO.getOption().add(optionDTO);
            }
            returnBillOrderItems.add(orderItemDTO);
        }
        returnValue.setOrderCustomer(GuestToGuestDTO.instance.convert(bill.getOrderCustomer()));
        returnValue.setOrderItems(returnBillOrderItems);
        return returnValue;
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

    public List<ProductDTO> generateProductDTO(List<Product> products) {
        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product : products) {
            ProductDTO productDTO = ProductToProductDTO.instance.convert(product);
            productDTO.setImgFileDTO(ImgFileToImgFileDTO.instance.convert(product.getImgFile()));
            productDTO.setProductType(product.getProductType());
            productDTO.setOptions(new ArrayList<>());
            for (Option option : product.getOptions()) {
                OptionDTO optionDTO = OptionToOptionDTO.instance.convert(option);
                optionDTO.setCheckItemList(new ArrayList<>());
                for (CheckItem checkItem : option.getCheckItemList()) {
                    CheckItemDTO checkItemDTO = CheckItemToCheckItemDTO.instance.convert(checkItem);
                    optionDTO.getCheckItemList().add(checkItemDTO);
                }
                productDTO.getOptions().add(optionDTO);
            }
            productDTOS.add(productDTO);
        }
        return productDTOS;
    }

    public ProductDTO generateProductDTO(Product product) {
        ProductDTO productDTO = ProductToProductDTO.instance.convert(product);
        productDTO.setImgFileDTO(ImgFileToImgFileDTO.instance.convert(product.getImgFile()));
        productDTO.setProductType(product.getProductType());
        productDTO.setOptions(new ArrayList<>());

        for (Option option : product.getOptions()) {
            OptionDTO optionDTO = OptionToOptionDTO.instance.convert(option);
            optionDTO.setCheckItemList(new ArrayList<>());
            for (CheckItem checkItem : option.getCheckItemList()) {
                CheckItemDTO checkItemDTO = CheckItemToCheckItemDTO.instance.convert(checkItem);
                optionDTO.getCheckItemList().add(checkItemDTO);
            }
            productDTO.getOptions().add(optionDTO);
        }
        return productDTO;
    }

    //RATE DTO UTILS

    public RateDTO generateRateDTO(Rate rate) {
        return RateToRateDTO.instance.convert(rate);
    }

    //RESTAURANT TABLE UTILS

    public RestaurentTableDTO generateRestaurentTableDTO(RestaurentTable restaurentTable) {

        RestaurentTableDTO restaurentTableDTO = RestaurentTableToRestaurenTableDTO.instance.convert(restaurentTable);
        restaurentTableDTO.setImgFileDTO(ImgFileToImgFileDTO.instance.convert(restaurentTable.getImgFile()));
        List<BillDTO> billDTOS = new ArrayList<>();
        if (Objects.nonNull(restaurentTable.getBills())) {
            restaurentTable.getBills().forEach(bill -> {
                BillDTO billDTO = generateBillDTOWithOrderItems(bill);
                billDTOS.add(billDTO);
            });
        }
        restaurentTableDTO.setBills(billDTOS);
        return restaurentTableDTO;
    }

    // ORDER ITEM DTO UTILS

    public OrderItemDTO generateOrderItemDTO(OrderItem orderItem) {
        return OrderItemToOrderItemDTO.instance.convert(orderItem);
    }

    // RESTAURANT DTO UTILS

    public RestaurantDTO generateRestaurantDTO(Restaurant restaurant) {
        RestaurantDTO restaurantDTO = RestaurantToRestaurantDTO.instance.convert(restaurant);
        restaurantDTO.setRestaurentTables(new ArrayList<>());
        restaurant.getRestaurentTables().forEach(restaurentTable -> {
            restaurantDTO.getRestaurentTables().add(generateRestaurentTableDTO(restaurentTable));
        });
        return restaurantDTO;
    }

}
