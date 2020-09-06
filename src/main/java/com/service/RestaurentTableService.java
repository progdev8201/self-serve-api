package com.service;

import com.mapping.BillToBillDTO;
import com.mapping.RestaurentTableToRestaurenTableDTO;
import com.model.dto.BillDTO;
import com.model.dto.RestaurentTableDTO;
import com.model.entity.Bill;
import com.model.entity.Restaurant;
import com.model.entity.RestaurentTable;
import com.repository.RestaurantRepository;
import com.repository.RestaurentTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RestaurentTableService {

    @Autowired
    RestaurentTableRepository restaurentTableRepository;

    @Autowired
    RestaurantRepository restaurantRepository;


    public List<RestaurentTableDTO> findAllForRestaurent(Long restaurentId) {
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

    public void deleteBillFromTable(Bill bill) {
        RestaurentTable restaurentTable = bill.getRestaurentTable();
        if(restaurentTable.getBill().remove(bill)){
            restaurentTable =restaurentTableRepository.save(restaurentTable);
        }

    }
}
