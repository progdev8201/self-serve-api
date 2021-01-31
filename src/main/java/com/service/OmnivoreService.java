package com.service;

import com.mapping.OmnivoreItemToProduct;
import com.mapping.OmnivoreLocationToRestaurant;
import com.mapping.OmnivoreMenuToMenu;
import com.mapping.OmnivoreTableToRestaurantTable;
import com.model.entity.Menu;
import com.model.entity.Product;
import com.model.entity.Restaurant;
import com.model.entity.RestaurentTable;
import com.model.omnivore.*;
import com.service.feign.OmnivoreClient;
import com.service.feign.OmnivoreItemClient;
import com.service.feign.OmnivoreMenuClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OmnivoreService {
    @Autowired
    private OmnivoreClient omnivoreClient;

    @Autowired
    private OmnivoreItemClient omnivoreItemClient;

    @Autowired
    private OmnivoreMenuClient omnivoreMenuClient;

    @Value("${omnivore.apiKey}")
    private String apiKey;


    public Restaurant createRestaurantFromLocation(String locationId) {
        OmnivoreLocation omnivoreLocation = omnivoreClient.findLocationById(locationId, apiKey);
        Restaurant restaurant = OmnivoreLocationToRestaurant.instance.convert(omnivoreLocation);
        restaurant.setRestaurentTables(createRestaurantTablesFromOmnivoreTables(restaurant.getLocationId()));
        return restaurant;
    }

    public Product createProductFromOmnivoreItem(String locationId, String menuId, String sectionId, String itemId){
        OmnivoreItem omnivoreItem = omnivoreItemClient.findItemByIdFromMenuSection(locationId,apiKey,menuId,sectionId,itemId);

        return OmnivoreItemToProduct.instance.convert(omnivoreItem);
    }

    public List<Product> createProductsFromOmnivoreItems(String locationId,String menuId,String sectionId){
        OmnivoreItemList omnivoreItemList = omnivoreItemClient.findAllItemsFromMenuSection(locationId,apiKey,menuId,sectionId);

        return omnivoreItemList.getOmnivoreItems()
                .stream()
                .map(OmnivoreItemToProduct.instance::convert)
                .collect(Collectors.toList());
    }

    public Menu createMenuFromOmnivoreMenu(String locationId,String menuId){
        OmnivoreMenu omnivoreMenu = omnivoreMenuClient.findMenuById(locationId,apiKey,menuId);

        return OmnivoreMenuToMenu.instance.convert(omnivoreMenu);
    }

    public List<Menu> createMenusFromOmnivoreMenus(String locationId){
        OmnivoreMenuList omnivoreMenuList = omnivoreMenuClient.findAllMenus(locationId,apiKey);

        return omnivoreMenuList.getOmnivoreMenus()
                .stream()
                .map( OmnivoreMenuToMenu.instance::convert)
                .collect(Collectors.toList());
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
