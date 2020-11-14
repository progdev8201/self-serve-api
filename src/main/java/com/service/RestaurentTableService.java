package com.service;

import com.mapping.BillToBillDTO;
import com.mapping.OrderItemToOrderItemDTO;
import com.mapping.RestaurentTableToRestaurenTableDTO;
import com.model.dto.*;
import com.model.entity.*;
import com.repository.BillRepository;
import com.repository.RestaurantRepository;
import com.repository.RestaurentTableRepository;
import com.service.DtoUtil.DTOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RestaurentTableService {

    @Autowired
    RestaurentTableRepository restaurentTableRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    BillRepository billRepository;

    @Autowired
    DTOUtils dtoUtils;


    public List<RestaurentTableDTO> findAllForRestaurent(Long restaurentId) {
        Restaurant restaurant = restaurantRepository.findById(restaurentId).get();
        List<RestaurentTableDTO> restaurentTableDTOS = new ArrayList<>();
        restaurant.getRestaurentTables().forEach(restaurentTable -> {
            restaurentTableDTOS.add(dtoUtils.generateRestaurentTableDTO(restaurentTable));
        });
        return restaurentTableDTOS;
    }

    public void deleteBillFromTable(Bill bill) {
        RestaurentTable restaurentTable = bill.getRestaurentTable();
        if(restaurentTable.getBills().remove(bill)){
            restaurentTable =restaurentTableRepository.save(restaurentTable);
        }
    }


}
