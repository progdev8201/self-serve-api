package com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.dto.BillDTO;
import com.model.dto.MenuDTO;
import com.model.dto.ProductDTO;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class MenuControllerTest {
    @Autowired
    MenuController menuController;

    @Test
    public void testCreateMakeAjouterSpecial() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String,String> requestParams = new LinkedMultiValueMap<>();

        MenuDTO menuDTO = createMenuDTO();
        BillDTO billDTO = new BillDTO();
        ObjectMapper objectMapper =new ObjectMapper();


        JSONObject sendObj = new JSONObject();
        sendObj.put("menu",objectMapper.writeValueAsString(menuDTO));


        MvcResult result= mvc.perform(MockMvcRequestBuilders.post(   "/menu/changeFeatured").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();

        MenuDTO reponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(),MenuDTO.class);
        assertEquals(2, reponse.getSpeciaux().get(0).getId());
    }
    @Test
    public void testCreateMakeAjouterSpecialSpecialDejaPresent() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String,String> requestParams = new LinkedMultiValueMap<>();

        MenuDTO menuDTO = createMenuDTO();
        ObjectMapper objectMapper =new ObjectMapper();


        JSONObject sendObj = new JSONObject();
        sendObj.put("menu",objectMapper.writeValueAsString(menuDTO));

        mvc.perform(MockMvcRequestBuilders.post(   "/menu/changeFeatured").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();

        menuDTO = createMenuDTO();
        menuDTO.getProducts().get(0).setId(3);
        sendObj = new JSONObject();
        sendObj.put("menu",objectMapper.writeValueAsString(menuDTO));

        MvcResult result= mvc.perform(MockMvcRequestBuilders.post(   "/menu/changeFeatured").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();

        MenuDTO reponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(),MenuDTO.class);
        assertEquals(2, reponse.getSpeciaux().size());
    }

    @Test
    public void testRemoveSpecial() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String,String> requestParams = new LinkedMultiValueMap<>();

        MenuDTO menuDTO = createMenuDTO();
        BillDTO billDTO = new BillDTO();
        ObjectMapper objectMapper =new ObjectMapper();


        JSONObject sendObj = new JSONObject();
        sendObj.put("menu",objectMapper.writeValueAsString(menuDTO));


        MvcResult result =mvc.perform(MockMvcRequestBuilders.post(   "/menu/changeFeatured").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        MenuDTO reponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(),MenuDTO.class);
        assertEquals(1, reponse.getSpeciaux().size());

         result= mvc.perform(MockMvcRequestBuilders.post(   "/menu/removeFeatured").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();

         reponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(),MenuDTO.class);
        assertEquals(0, reponse.getSpeciaux().size());
    }

    private MenuDTO createMenuDTO() {
        List<ProductDTO> productDTOS = new ArrayList<>();
        MenuDTO menuDTO = new MenuDTO();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(2);
        productDTOS.add(productDTO);
        menuDTO.setId(4L);
        menuDTO.setProducts(productDTOS);
        return menuDTO;
    }

    private MockMvc initMockMvc(){
        return MockMvcBuilders.standaloneSetup(menuController).build();
    }

}