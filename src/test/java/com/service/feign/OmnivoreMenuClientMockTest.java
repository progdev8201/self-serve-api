package com.service.feign;

import com.model.omnivore.OmnivoreMenu;
import com.model.omnivore.OmnivoreMenuList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;

@ExtendWith(MockitoExtension.class)
class OmnivoreMenuClientMockTest {

    @Mock
    private OmnivoreMenuClient omnivoreMenuClient;

    @Test
    public void fetchMenuByIdTest(){
        // Arrange

        when(omnivoreMenuClient.findMenuById(anyString(),anyString(),anyString())).thenReturn(initMenu());

        // Act

        OmnivoreMenu omnivoreMenu = omnivoreMenuClient.findMenuById("test","test","test");

        // Assert

        assertNotNull(omnivoreMenu.getCreated());
        assertNotNull(omnivoreMenu.getModified());
        assertNotNull(omnivoreMenu.getId());
        assertNotNull(omnivoreMenu.getName());
        assertNotNull(omnivoreMenu.getType());
    }

    @Test
    public void findAllMenusTest(){
        // Arrange

        final long COUNT = 3l;
        final long LIMIT = 100l;
        when(omnivoreMenuClient.findAllMenus(anyString(),anyString())).thenReturn(initMenuList(COUNT,LIMIT));

        // Act

        OmnivoreMenuList omnivoreMenuList = omnivoreMenuClient.findAllMenus("test","test");

        // Assert

        assertEquals(omnivoreMenuList.getCount(),COUNT);
        assertEquals(omnivoreMenuList.getLimit(),LIMIT);
        assertEquals(omnivoreMenuList.getOmnivoreMenuList().size(),COUNT);
    }

    private OmnivoreMenu initMenu(){
        OmnivoreMenu omnivoreMenu = new OmnivoreMenu();

        omnivoreMenu.setId("koTjETdj");
        omnivoreMenu.setName("Omnivore Cantina");
        omnivoreMenu.setType("delivery");
        omnivoreMenu.setCreated(LocalDateTime.now());
        omnivoreMenu.setModified(LocalDateTime.now());

        return omnivoreMenu;
    }

    private OmnivoreMenuList initMenuList(long count,long limit){
        OmnivoreMenuList omnivoreMenuList = new OmnivoreMenuList();

        omnivoreMenuList.setCount(count);
        omnivoreMenuList.setLimit(limit);
        omnivoreMenuList.setOmnivoreMenuList(Arrays.asList(initMenu(),initMenu(),initMenu()));

        return omnivoreMenuList;
    }

}