package com.service.DtoUtil;

import com.mapping.BillToBillDTO;
import com.mapping.GuestToGuestDTO;
import com.mapping.OrderItemToOrderItemDTO;
import com.mapping.ProductToProductDTO;
import com.model.dto.BillDTO;
import com.model.dto.OrderItemDTO;
import com.model.entity.Bill;
import com.model.entity.OrderItem;
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
            orderItemDTO.setProduct(ProductToProductDTO.instance.convert(orderItem.getProduct()));
            returnBillOrderItems.add(orderItemDTO);
        }
        returnValue.setOrderCustomer(GuestToGuestDTO.instance.convert(bill.getOrderCustomer()));
        returnValue.setOrderItems(returnBillOrderItems);
        return returnValue;
    }
}
