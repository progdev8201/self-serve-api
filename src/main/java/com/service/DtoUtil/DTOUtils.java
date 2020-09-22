package com.service.DtoUtil;

import com.mapping.*;
import com.model.dto.*;
import com.model.entity.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DTOUtils {
    public BillDTO constructBillDTOWithOrderItems(Bill bill) {
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
    private MenuDTO returnMenu(Menu menu) {
        MenuDTO returnValue = MenuToMenuDTOImpl.instance.convert(menu);
        returnValue.setSpeciaux(new ArrayList<>());
        for (Product special : menu.getSpeciaux()) {
            returnValue.getSpeciaux().add(ProductToProductDTO.instance.convert(special));
            returnValue.setSpeciaux(returnValue.getSpeciaux());
        }
        return returnValue;
    }
}
