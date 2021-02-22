package com.service;

import com.model.entity.Menu;
import com.model.entity.Product;
import com.model.entity.Restaurant;
import com.model.entity.RestaurentTable;
import com.model.omnivore.*;
import com.service.feign.OmnivoreClient;
import com.service.feign.OmnivoreItemClient;
import com.service.feign.OmnivoreMenuClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
    private final String ITEM_MENU_TEST_STRINGS = "test";
    private final LocalDateTime GLOBAL_TIME = LocalDateTime.now();
    private final BigDecimal BASIC_PRICE = BigDecimal.valueOf(5.99);
    private final int nombreDeTables = 3;
    private final long COUNT = 3l;
    private final long LIMIT = 100l;


    @BeforeEach
    public void setFields() {
        ReflectionTestUtils.setField(omnivoreService, "apiKey", "123-456-789");
    }

    @Test
    public void testCreateRestaurantFromLocation() {
        // Arrange

        Mockito.when(omnivoreClient.findLocationById(anyString(), anyString())).thenReturn(initLocation());
        Mockito.when(omnivoreClient.findAllTables(anyString(), anyString())).thenReturn(initOmnivoreTableList());

        // Act

        Restaurant restaurant = omnivoreService.createRestaurantFromLocation(locationID);

        // Assert

        assertEquals(locationID, restaurant.getLocationId());
        assertEquals(restaurantName, restaurant.getName());

        for (int i = 0; i < nombreDeTables; i++) {
            RestaurentTable restaurentTable = restaurant.getRestaurentTables().get(i);
            assertEquals(i, restaurentTable.getOmnivoreTableID());
            assertEquals(i, restaurentTable.getTableNumber());
            assertEquals(i, restaurentTable.getSeats());
        }
    }

    @Test
    public void createProductFromOmnivoreItemTest() {
        // Arrange

        when(omnivoreItemClient.findItemByIdFromMenuSection(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(initItem());

        // Act

        Product product = omnivoreService.createProductFromOmnivoreItem("test", "test", "test", "test");

        // Assert

        assertEquals(product.getDescription(),ITEM_MENU_TEST_STRINGS);
        assertEquals(product.getOmnivoreItemId(),ITEM_MENU_TEST_STRINGS);
        assertEquals(product.getName(),ITEM_MENU_TEST_STRINGS);
        assertEquals(product.getPrix(),BASIC_PRICE);
    }

    @Test
    public void createProductsFromOmnivoreItemsTest() {
        // Arrange

        when(omnivoreItemClient.findAllItemsFromMenuSection(anyString(), anyString(), anyString(), anyString())).thenReturn(initItemList());

        // Act

        List<Product> products = omnivoreService.createProductsFromOmnivoreItems("test", "test", "test");

        // Assert

        products.forEach(product -> {
            assertEquals(product.getDescription(),ITEM_MENU_TEST_STRINGS);
            assertEquals(product.getOmnivoreItemId(),ITEM_MENU_TEST_STRINGS);
            assertEquals(product.getName(),ITEM_MENU_TEST_STRINGS);
            assertEquals(product.getPrix(),BASIC_PRICE);
        });

        assertFalse(products.isEmpty());
    }

    @Test
    public void createMenuFromOmnivoreMenuTest() {
        // Arrange

        when(omnivoreMenuClient.findMenuById(anyString(), anyString(), anyString())).thenReturn(initMenu());

        // Act

        Menu menu = omnivoreService.createMenuFromOmnivoreMenu("test", "test");

        // Assert

        assertEquals(menu.getOmnivoreMenuId(),ITEM_MENU_TEST_STRINGS);
        assertEquals(menu.getOmnivoreMenuType(),ITEM_MENU_TEST_STRINGS);
        assertEquals(menu.getName(),ITEM_MENU_TEST_STRINGS);
    }

    @Test
    public void createMenusFromOmnivoreMenusTest() {
        // Arrange

        when(omnivoreMenuClient.findAllMenus(anyString(), anyString())).thenReturn(initMenuList());

        // Act

        List<Menu> menus = omnivoreService.createMenusFromOmnivoreMenus("test");

        // Assert

        menus.forEach(menu -> {
            assertEquals(menu.getOmnivoreMenuId(),ITEM_MENU_TEST_STRINGS);
            assertEquals(menu.getOmnivoreMenuType(),ITEM_MENU_TEST_STRINGS);
            assertEquals(menu.getName(),ITEM_MENU_TEST_STRINGS);
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

    private OmnivoreItem initItem() {
        OmnivoreItem omnivoreItem = new OmnivoreItem();

        omnivoreItem.setDescription(ITEM_MENU_TEST_STRINGS);
        omnivoreItem.setId(ITEM_MENU_TEST_STRINGS);
        omnivoreItem.setModified(LocalDateTime.now());
        omnivoreItem.setName(ITEM_MENU_TEST_STRINGS);
        omnivoreItem.setPricePerUnit(BASIC_PRICE);

        return omnivoreItem;
    }

    private OmnivoreItemList initItemList() {
        OmnivoreItemList omnivoreItemList = new OmnivoreItemList();

        omnivoreItemList.setCount(COUNT);
        omnivoreItemList.setLimit(LIMIT);
        omnivoreItemList.setOmnivoreItems(Arrays.asList(initItem(), initItem(), initItem()));

        return omnivoreItemList;
    }

    private OmnivoreMenu initMenu() {
        OmnivoreMenu omnivoreMenu = new OmnivoreMenu();

        omnivoreMenu.setId(ITEM_MENU_TEST_STRINGS);
        omnivoreMenu.setName(ITEM_MENU_TEST_STRINGS);
        omnivoreMenu.setType(ITEM_MENU_TEST_STRINGS);
        omnivoreMenu.setCreated(LocalDateTime.now());
        omnivoreMenu.setModified(LocalDateTime.now());

        return omnivoreMenu;
    }

    private OmnivoreMenuList initMenuList() {
        OmnivoreMenuList omnivoreMenuList = new OmnivoreMenuList();

        omnivoreMenuList.setCount(COUNT);
        omnivoreMenuList.setLimit(LIMIT);
        omnivoreMenuList.setOmnivoreMenus(Arrays.asList(initMenu(), initMenu(), initMenu()));

        return omnivoreMenuList;
    }


}