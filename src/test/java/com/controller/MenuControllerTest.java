package com.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.model.dto.*;
import com.model.entity.Menu;
import com.model.entity.Restaurant;
import com.model.enums.MenuType;
import com.repository.RestaurantRepository;
import com.service.MenuCreationService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
public class MenuControllerTest {
    @Autowired
    MenuController menuController;

    @Autowired
    MenuCreationService menuCreationService;

    @MockBean
    RestaurantRepository restaurantRepository;

    @Test
    public void testFindAllMenuForRestaurant() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();


        JSONObject sendObj = new JSONObject();
        sendObj.put("restaurantId", "2");
        Mockito.when(restaurantRepository.findById(any(Long.class))).thenReturn(initRestaurant());
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/menu/getMenu").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        List<MenuDTO> reponse = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<MenuDTO>>(){});
        assertEquals(3, reponse.size());
    }
    @Test
    public void testCreateMenu() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();


        JSONObject sendObj = new JSONObject();
        sendObj.put("restaurantId", "1");
        sendObj.put("menuName", "le menu bien bon");

        Mockito.when(restaurantRepository.findById(any(Long.class))).thenReturn(initRestaurant());
        Mockito.when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                return (Restaurant) invocation.getArguments()[0];
            }
        });

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/menu/createMenu").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        MenuDTO reponse = mapper.readValue(result.getResponse().getContentAsString(), MenuDTO.class);
        assertEquals("le menu bien bon",reponse.getName());
    }
    @Test
    public void testCreateWithSameNameTwiceBadRequest() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();


        JSONObject sendObj = new JSONObject();
        sendObj.put("restaurantId", "1");
        sendObj.put("menuName", "le menu bien bon bon");
        Restaurant resto = initRestaurant().get();
        resto.getMenus().get(0).setName("le menu bien bon bon");

        Mockito.when(restaurantRepository.findById(any(Long.class))).thenReturn(Optional.of(resto));
        Mockito.when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                return (Restaurant) invocation.getArguments()[0];
            }
        });

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/menu/createMenu").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andReturn();
        String value= result.getResponse().getContentAsString();
        assertEquals("Fail -> Menu with same name already exists",value);
    }


    private MockMvc initMockMvc() {
        return MockMvcBuilders.standaloneSetup(menuController).build();
    }
    private Optional<Restaurant> initRestaurant() throws IOException {
        Restaurant restaurant = new Restaurant();
        restaurant.setMenus(initListMenu());
        return  Optional.of(restaurant);
    }
    private List<Menu> initListMenu() throws IOException {
        List<Menu> menus = new ArrayList<>();
        for(int i=0;i<3;i++){
            Menu menu = new Menu();
            menu.setName("menu chic"+i);
            menu.setProducts(new ArrayList<>());
            menuCreationService.createProduct(MenuType.FOOD, "download.jpg", 29.99 +i, 30+i, "Steak chico souper " +i, menu);
            menu.setMenuType(MenuType.FOOD);
            menus.add(menu);
        }
        for(int i=0;i<3;i++){
            Menu menu = new Menu();
            menu.setName("menu pas chick"+i);
            menu.setMenuType(MenuType.WAITERREQUEST);
            menus.add(menu);
        }
        return menus;
    }

}
