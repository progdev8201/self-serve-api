package com.service;

import com.mapping.BillToBillDTO;
import com.mapping.RestaurentTableToRestaurenTableDTO;
import com.model.dto.BillDTO;
import com.model.dto.RestaurentTableDTO;
import com.model.entity.Restaurant;
import com.repository.RestaurantRepository;
import com.repository.RestaurentTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurentTableService {

    @Autowired
    RestaurentTableRepository restaurentTableRepository;

    @Autowired
    RestaurantRepository restaurantRepository;


    public List<RestaurentTableDTO> findAllForRestaurent(Long restaurentId){
        Restaurant restaurant = restaurantRepository.findById(restaurentId).get();
        List<RestaurentTableDTO> restaurentTableDTOS = new ArrayList<>();
        restaurant.getRestaurentTables().forEach(restaurentTable -> {

            RestaurentTableDTO restaurentTableDTO = RestaurentTableToRestaurenTableDTO.instance.convert(restaurentTable);
            List<BillDTO> billDTOS = new ArrayList<>();
            restaurentTable.getBill().forEach(bill -> {
                billDTOS.add(BillToBillDTO.instance.convert(bill));
            });
            restaurentTableDTO.setBillDTOList(billDTOS);
            restaurentTableDTOS.add(restaurentTableDTO);

        });



        return restaurentTableDTOS;
    }
}
