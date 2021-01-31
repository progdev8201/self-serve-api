package com.service.feign;

import com.model.omnivore.OmnivoreItem;
import com.model.omnivore.OmnivoreItemList;
import com.model.omnivore.OmnivoreMenu;
import com.model.omnivore.OmnivoreMenuList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OmnivoreItemClientMockTest {

    @Mock
    private OmnivoreItemClient omnivoreItemClient;

    @Test
    public void fetchItemByIdTest(){
        // Arrange

        when(omnivoreItemClient.findItemByIdFromMenuSection(anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(initItem());

        // Act

        OmnivoreItem omnivoreItem = omnivoreItemClient.findItemByIdFromMenuSection("test","test","test","test","test");

        // Assert

        assertNotNull(omnivoreItem.getPricePerUnit());
        assertNotNull(omnivoreItem.getName());
        assertNotNull(omnivoreItem.getId());
        assertNotNull(omnivoreItem.getDescription());
        assertNotNull(omnivoreItem.getModified());
    }

    @Test
    public void findAllItemsTest(){
        // Arrange

        when(omnivoreItemClient.findAllItemsFromMenuSection(anyString(),anyString(),anyString(),anyString())).thenReturn(initMenuList());

        // Act

        OmnivoreItemList omnivoreItemList = omnivoreItemClient.findAllItemsFromMenuSection("test","test","test","test");

        // Assert

        assertNotNull(omnivoreItemList.getCount());
        assertNotNull(omnivoreItemList.getLimit());
        assertFalse(omnivoreItemList.getOmnivoreItems().isEmpty());
    }

    public static OmnivoreItem initItem(){
       OmnivoreItem omnivoreItem = new OmnivoreItem();

       omnivoreItem.setDescription("test");
       omnivoreItem.setId("test");
       omnivoreItem.setModified(LocalDateTime.now());
       omnivoreItem.setName("test");
       omnivoreItem.setPricePerUnit(BigDecimal.valueOf(5.99));

       return omnivoreItem;
    }

    public static OmnivoreItemList initMenuList(){
        OmnivoreItemList omnivoreItemList = new OmnivoreItemList();

        omnivoreItemList.setCount(3l);
        omnivoreItemList.setLimit(100l);
        omnivoreItemList.setOmnivoreItems(Arrays.asList(initItem(),initItem(),initItem()));

        return omnivoreItemList;
    }

}