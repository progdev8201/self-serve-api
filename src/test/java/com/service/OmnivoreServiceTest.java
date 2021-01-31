package com.service;

import com.model.entity.Menu;
import com.model.entity.Product;
import com.model.entity.Restaurant;
import com.model.entity.RestaurentTable;
import com.model.omnivore.OmnivoreLocation;
import com.model.omnivore.OmnivoreTable;
import com.model.omnivore.OmnivoreTableList;
import com.service.feign.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class OmnivoreServiceTest {
    @InjectMocks
    private OmnivoreService omnivoreService;

    @Mock
    private OmnivoreClient omnivoreClient;

    @Mock
    private OmnivoreItemClient omnivoreItemClient;

    @Mock
    private OmnivoreMenuClient omnivoreMenuClient;

    private final String locationID = "123abc";
    private final String restaurantName = "le resto chouette";
    private final String restaurantOwner = "le owner pas trop chouette";
    private final String tableName = "ta table chico";
    private final int nombreDeTables = 3;

    @BeforeEach
    public void setFields() {
        ReflectionTestUtils.setField(omnivoreService, "apiKey", "123-456-789");
    }

    @Test
    public void testCreateRestaurantFromLocation() {
        Mockito.when(omnivoreClient.findLocationById(anyString(), anyString())).thenReturn(initLocation());
        Mockito.when(omnivoreClient.findAllTables(anyString(), anyString())).thenReturn(initOmnivoreTableList());
        Restaurant restaurant = omnivoreService.createRestaurantFromLocation(locationID);
        assertEquals(locationID, restaurant.getLocationId());
        assertEquals(restaurantName, restaurant.getName());
        for (int i = 0; i < nombreDeTables; i++) {
            RestaurentTable restaurentTable = restaurant.getRestaurentTables().get(i);
            assertEquals(i, restaurentTable.getOmnivoreTableID());
            assertEquals(i, restaurentTable.getTableNumber());
            assertEquals(i,restaurentTable.getSeats());
        }
    }

    @Test
    public void createProductFromOmnivoreItemTest(){
        // Arrange

        when(omnivoreItemClient.findItemByIdFromMenuSection(anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(OmnivoreItemClientMockTest.initItem());

        // Act

        Product product = omnivoreService.createProductFromOmnivoreItem("test","test","test","test");

        // Assert

        assertNotNull(product.getDescription());
        assertNotNull(product.getOmnivoreItemId());
        assertNotNull(product.getName());
        assertNotNull(product.getPrix());
    }

    @Test
    public void createProductsFromOmnivoreItemsTest(){
        // Arrange

        when(omnivoreItemClient.findAllItemsFromMenuSection(anyString(),anyString(),anyString(),anyString())).thenReturn(OmnivoreItemClientMockTest.initMenuList());

        // Act

        List<Product> products = omnivoreService.createProductsFromOmnivoreItems("test","test","test");

        // Assert

        products.forEach(product -> {
            assertNotNull(product.getDescription());
            assertNotNull(product.getOmnivoreItemId());
            assertNotNull(product.getName());
            assertNotNull(product.getPrix());
        });

        assertFalse(products.isEmpty());
    }

    @Test
    public void createMenuFromOmnivoreMenuTest(){
        // Arrange

        when(omnivoreMenuClient.findMenuById(anyString(),anyString(),anyString())).thenReturn(OmnivoreMenuClientMockTest.initMenu());

        // Act

        Menu menu = omnivoreService.createMenuFromOmnivoreMenu("test","test");

        // Assert

        assertNotNull(menu.getOmnivoreMenuId());
        assertNotNull(menu.getOmnivoreMenuType());
        assertNotNull(menu.getName());
    }

    @Test
    public void createMenusFromOmnivoreMenusTest(){
        // Arrange

        when(omnivoreMenuClient.findAllMenus(anyString(),anyString())).thenReturn(OmnivoreMenuClientMockTest.initMenuList());

        // Act

        List<Menu> menus = omnivoreService.createMenusFromOmnivoreMenus("test");

        // Assert

        menus.forEach(menu -> {
            assertNotNull(menu.getOmnivoreMenuId());
            assertNotNull(menu.getOmnivoreMenuType());
            assertNotNull(menu.getName());
        });

        assertFalse(menus.isEmpty());
    }

    private OmnivoreLocation initLocation() {
        OmnivoreLocation omnivoreLocation = new OmnivoreLocation();
        omnivoreLocation.setId(locationID);
        omnivoreLocation.setName(restaurantName);
        omnivoreLocation.setOwner(restaurantOwner);
        return omnivoreLocation;
    }

    private OmnivoreTableList initOmnivoreTableList() {
        List<OmnivoreTable> omnivoreTables = new ArrayList<>();
        for (int i = 0; i < nombreDeTables; i++) {
            OmnivoreTable omnivoreTable = new OmnivoreTable();
            omnivoreTable.setId(Long.valueOf(i));
            omnivoreTable.setName(tableName + i);
            omnivoreTable.setSeats(Long.valueOf(i));
            omnivoreTable.setNumber(Long.valueOf(i));
            omnivoreTables.add(omnivoreTable);
        }
        OmnivoreTableList omnivoreLocation = new OmnivoreTableList();
        omnivoreLocation.setOmnivoreTableList(omnivoreTables);
        return omnivoreLocation;
    }

}