package com.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.model.dto.*;
import com.model.enums.MenuType;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
public class MenuControllerTest {
    @Autowired
    MenuController menuController;


    @Test
    public void testFindAllMenuFoodForRestaurant() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();


        JSONObject sendObj = new JSONObject();
        sendObj.put("restaurantId", "2");

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
    public void testFindAllMenuForRestaurant() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();


        JSONObject sendObj = new JSONObject();
        sendObj.put("restaurantId", "2");

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/menu/getAllMenu").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        List<MenuDTO> reponse = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<MenuDTO>>(){});
        assertEquals(4, reponse.size());
    }
    @Test
    public void testCreateMenu() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();


        JSONObject sendObj = new JSONObject();
        sendObj.put("restaurantId", "1");
        sendObj.put("menuName", "le menu bien bon");
        sendObj.put("menuType", MenuType.FOOD);

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
        sendObj.put("menuType", "FOOD");

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/menu/createMenu").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        MenuDTO reponse = mapper.readValue(result.getResponse().getContentAsString(), MenuDTO.class);
        assertEquals("le menu bien bon bon",reponse.getName());

        result = mvc.perform(MockMvcRequestBuilders.post("/menu/createMenu").
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

}
