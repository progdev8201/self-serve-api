package com.service;

import com.model.dto.RestaurentTableDTO;
import com.model.entity.Bill;
import com.model.entity.Restaurant;
import com.model.entity.RestaurentTable;
import com.repository.RestaurantRepository;
import com.repository.RestaurentTableRepository;
import com.service.Util.DTOUtils;
import com.service.validator.RestaurantOwnerShipValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RestaurentTableService {

    @Autowired
    private RestaurentTableRepository restaurentTableRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private DTOUtils dtoUtils;

    @Autowired
    private RestaurantOwnerShipValidator restaurantOwnerShipValidator;


    public ResponseEntity<List<RestaurentTableDTO>> findAllForRestaurent(Long restaurentId) {
        if (!restaurantOwnerShipValidator.hasCookWaiterRight(restaurentId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();


        Restaurant restaurant = restaurantRepository.findById(restaurentId).get();
        List<RestaurentTableDTO> restaurentTableDTOS = new ArrayList<>();
        restaurant.getRestaurentTables().forEach(restaurentTable -> {
            restaurentTableDTOS.add(dtoUtils.mapRestaurantTableToRestaurantTableDTO(restaurentTable));
        });
        return ResponseEntity.ok(restaurentTableDTOS);
    }



    public void deleteBillFromTable(Bill bill) {
        RestaurentTable restaurentTable = bill.getRestaurentTable();
        if (restaurentTable.getBills().remove(bill)) {
            restaurentTable = restaurentTableRepository.save(restaurentTable);
        }
    }


}
