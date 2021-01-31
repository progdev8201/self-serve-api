package com.service;

import com.mapping.OmnivoreLocationToRestaurant;
import com.mapping.OmnivoreTableToRestaurantTable;
import com.model.entity.Restaurant;
import com.model.entity.RestaurentTable;
import com.model.omnivore.OmnivoreLocation;
import com.model.omnivore.OmnivoreTableList;
import com.service.feign.OmnivoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OmnivoreService {
    @Autowired
    OmnivoreClient omnivoreClient;

    @Value("${omnivore.apiKey}")
    private String apiKey;


    public Restaurant createRestaurantFromLocation(String locationId) {
        OmnivoreLocation omnivoreLocation = omnivoreClient.findLocationById(locationId, apiKey);
        Restaurant restaurant = OmnivoreLocationToRestaurant.instance.convert(omnivoreLocation);
        restaurant.setRestaurentTables(createRestaurantTablesFromOmnivoreTables(restaurant.getLocationId()));
        return restaurant;
    }

    private List<RestaurentTable> createRestaurantTablesFromOmnivoreTables(String locationId) {
        OmnivoreTableList omnivoreTableList = omnivoreClient.findAllTables(locationId, apiKey);
        return omnivoreTableList
                .getOmnivoreTableList()
                .stream()
                .map(omnivoreTable -> OmnivoreTableToRestaurantTable.instance.convert(omnivoreTable))
                .collect(Collectors.toList());
    }
}
