package com.service.DtoUtil;

import com.mapping.*;
import com.model.dto.*;
import com.model.entity.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DTOUtils {
    public BillDTO generateBillDTOWithOrderItems(Bill bill) {
        BillDTO returnValue = BillToBillDTO.instance.convert(bill);
        List<OrderItemDTO> returnBillOrderItems = new ArrayList<>();
        for (OrderItem orderItem : bill.getOrderItems()) {
            OrderItemDTO orderItemDTO = OrderItemToOrderItemDTO.instance.convert(orderItem);
            ProductDTO productDTO =ProductToProductDTO.instance.convert(orderItem.getProduct());
            ImgFileDTO imgFileDTO =ImgFileToImgFileDTO.instance.convert(orderItem.getProduct().getImgFile());
            productDTO.setImgFileDTO(imgFileDTO);
            orderItemDTO.setProduct(productDTO);
            orderItemDTO.setOption(new ArrayList<>());
            for(Option option :orderItem.getOption()){
                OptionDTO optionDTO = OptionToOptionDTO.instance.convert(option);
                optionDTO.setCheckItemList(new ArrayList<>());
                for (CheckItem checkItem : option.getCheckItemList())
                {
                    CheckItemDTO checkItemDTO =CheckItemToCheckItemDTO.instance.convert(checkItem);
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
    public MenuDTO generateMenuDTO(Menu menu) {
        MenuDTO returnValue = MenuToMenuDTOImpl.instance.convert(menu);
        returnValue.setSpeciaux(new ArrayList<>());
        for (Product special : menu.getSpeciaux()) {
            returnValue.getSpeciaux().add(ProductToProductDTO.instance.convert(special));
            returnValue.setSpeciaux(returnValue.getSpeciaux());
        }
        return returnValue;
    }
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
    public RateDTO generateRateDTO(Rate rate){
        return RateToRateDTOImpl.instance.convert(rate);
    }
    public RestaurentTableDTO generateRestaurentTableDTO(RestaurentTable restaurentTable){

        RestaurentTableDTO restaurentTableDTO = RestaurentTableToRestaurenTableDTO.instance.convert(restaurentTable);
        restaurentTableDTO.setImgFileDTO(ImgFileToImgFileDTO.instance.convert(restaurentTable.getImgFile()));
        List<BillDTO> billDTOS = new ArrayList<>();
        restaurentTable.getBills().forEach(bill -> {
            BillDTO billDTO = generateBillDTOWithOrderItems(bill);
            billDTOS.add(billDTO);
        });
        restaurentTableDTO.setBills(billDTOS);
        return  restaurentTableDTO;
    }
    public OrderItemDTO generateOrderItemDTO(OrderItem orderItem){
        return OrderItemToOrderItemDTO.instance.convert(orderItem);
    }

}
