package com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.JsonObject;
import com.model.dto.BillDTO;
import com.model.dto.MenuDTO;
import com.model.dto.ProductDTO;
import com.model.entity.Product;
import com.model.enums.ProductType;
import com.repository.MenuRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ProductControllerTest {
    @Autowired
    ProductController productController;

    @Autowired
    MenuController menuController;
    @Test
    public void testSetSpecialPourProduit() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String,String> requestParams = new LinkedMultiValueMap<>();

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(3L);
        productDTO.setPrix(39.99);
        productDTO.setName("killua");
        ObjectMapper objectMapper =new ObjectMapper();


        JSONObject sendObj = new JSONObject();
        sendObj.put("productDTO",objectMapper.writeValueAsString(productDTO));

        MvcResult result= mvc.perform(MockMvcRequestBuilders.post(   "/product/setMenuSpecial").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ObjectMapper mapper=new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Product reponse = mapper.readValue(result.getResponse().getContentAsString(), Product.class);
        assertEquals(ProductType.SPECIAL, reponse.getProductType());

        JSONObject sendObj2 = new JSONObject();
        sendObj2.put("menuId","2");
        mvc = initMockMvcMenuController();
        result= mvc.perform(MockMvcRequestBuilders.get(   "/menu/getMenu").
                content("2").
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        MenuDTO menuDTOResponse = mapper.readValue(result.getResponse().getContentAsString(), MenuDTO.class);

        assertEquals(ProductType.SPECIAL, menuDTOResponse.getProducts().get(0).getProductType());
    }

    @Test
    public void testSetSpecialPourProduitChercherTypeProduitNonModifie() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String,String> requestParams = new LinkedMultiValueMap<>();

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(3L);
        productDTO.setPrix(39.99);
        productDTO.setName("killua");
        ObjectMapper objectMapper =new ObjectMapper();


        JSONObject sendObj = new JSONObject();
        sendObj.put("productDTO",objectMapper.writeValueAsString(productDTO));

        MvcResult result= mvc.perform(MockMvcRequestBuilders.post(   "/product/setMenuSpecial").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ObjectMapper mapper=new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Product reponse = mapper.readValue(result.getResponse().getContentAsString(), Product.class);
        assertEquals(ProductType.SPECIAL, reponse.getProductType());

        JSONObject sendObj2 = new JSONObject();
        sendObj2.put("menuId","2");
        mvc = initMockMvcMenuController();
        result= mvc.perform(MockMvcRequestBuilders.get(   "/menu/getMenu").
                content("2").
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        MenuDTO menuDTOResponse = mapper.readValue(result.getResponse().getContentAsString(), MenuDTO.class);

        assertEquals(null, menuDTOResponse.getProducts().get(1).getProductType());
    }

    @Test
    public void testSetChoixChefPourProduit() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String,String> requestParams = new LinkedMultiValueMap<>();

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(3L);
        productDTO.setPrix(39.99);
        productDTO.setName("killua");
        ObjectMapper objectMapper =new ObjectMapper();


        JSONObject sendObj = new JSONObject();
        sendObj.put("productDTO",objectMapper.writeValueAsString(productDTO));

        MvcResult result= mvc.perform(MockMvcRequestBuilders.post(   "/product/setMenuChefChoice").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ObjectMapper mapper=new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Product reponse = mapper.readValue(result.getResponse().getContentAsString(), Product.class);
        assertEquals(ProductType.CHEFCHOICE, reponse.getProductType());

        JSONObject sendObj2 = new JSONObject();
        sendObj2.put("menuId","2");
        mvc = initMockMvcMenuController();
        result= mvc.perform(MockMvcRequestBuilders.get(   "/menu/getMenu").
                content("2").
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        MenuDTO menuDTOResponse = mapper.readValue(result.getResponse().getContentAsString(), MenuDTO.class);

        assertEquals(ProductType.CHEFCHOICE, menuDTOResponse.getProducts().get(0).getProductType());
    }

    @Test
    public void testRemoveSpecialProduit() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String,String> requestParams = new LinkedMultiValueMap<>();

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(3L);
        productDTO.setPrix(39.99);
        productDTO.setName("killua");
        ObjectMapper objectMapper =new ObjectMapper();


        JSONObject sendObj = new JSONObject();
        sendObj.put("productDTO",objectMapper.writeValueAsString(productDTO));

        MvcResult result= mvc.perform(MockMvcRequestBuilders.post(   "/product/setMenuSpecial").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ObjectMapper mapper=new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Product reponse = mapper.readValue(result.getResponse().getContentAsString(), Product.class);
        assertEquals(ProductType.SPECIAL, reponse.getProductType());

         result= mvc.perform(MockMvcRequestBuilders.post(   "/product/deleteProductType").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ProductDTO productDTOResponse = mapper.readValue(result.getResponse().getContentAsString(), ProductDTO.class);

        assertEquals(null, productDTOResponse.getProductType());
    }

    @Test
    public void testGetListeSpecial() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String,String> requestParams = new LinkedMultiValueMap<>();

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(3L);
        productDTO.setPrix(39.99);
        productDTO.setName("killua");
        ObjectMapper objectMapper =new ObjectMapper();


        JSONObject sendObj = new JSONObject();
        sendObj.put("productDTO",objectMapper.writeValueAsString(productDTO));

        MvcResult result= mvc.perform(MockMvcRequestBuilders.post(   "/product/setMenuSpecial").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();


         productDTO = new ProductDTO();
        productDTO.setId(4L);
        productDTO.setPrix(69.99);
        productDTO.setName("killua");
         objectMapper =new ObjectMapper();


         sendObj = new JSONObject();
        sendObj.put("productDTO",objectMapper.writeValueAsString(productDTO));

         result= mvc.perform(MockMvcRequestBuilders.post(   "/product/setMenuSpecial").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();

        ObjectMapper mapper=new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Product reponse = mapper.readValue(result.getResponse().getContentAsString(), Product.class);
        assertEquals(ProductType.SPECIAL, reponse.getProductType());

        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setId(2L);
        sendObj = new JSONObject();
        sendObj.put("menuDTO",objectMapper.writeValueAsString(menuDTO));

        result= mvc.perform(MockMvcRequestBuilders.get(   "/product/findMenuSpecial").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        List<ProductDTO> productDTOResponse = mapper.readValue(result.getResponse().getContentAsString(), List.class);
        for(ProductDTO x : productDTOResponse){
            assertEquals(ProductType.SPECIAL, x.getProductType());
        }
    }

    @Test
    public void testGetListeChoixChef() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String,String> requestParams = new LinkedMultiValueMap<>();

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(3L);
        productDTO.setPrix(39.99);
        productDTO.setName("killua");
        ObjectMapper objectMapper =new ObjectMapper();


        JSONObject sendObj = new JSONObject();
        sendObj.put("productDTO",objectMapper.writeValueAsString(productDTO));

        MvcResult result= mvc.perform(MockMvcRequestBuilders.post(   "/product/setMenuChefChoice").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();


        productDTO = new ProductDTO();
        productDTO.setId(4L);
        productDTO.setPrix(69.99);
        productDTO.setName("killua");
        objectMapper =new ObjectMapper();


        sendObj = new JSONObject();
        sendObj.put("productDTO",objectMapper.writeValueAsString(productDTO));

        result= mvc.perform(MockMvcRequestBuilders.post(   "/product/setMenuChefChoice").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();

        ObjectMapper mapper=new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Product reponse = mapper.readValue(result.getResponse().getContentAsString(), Product.class);
        assertEquals(ProductType.CHEFCHOICE, reponse.getProductType());

        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setId(2L);
        sendObj = new JSONObject();
        sendObj.put("menuDTO",objectMapper.writeValueAsString(menuDTO));

        result= mvc.perform(MockMvcRequestBuilders.get(   "/product/findChoixDuChef").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        List<ProductDTO> productDTOResponse = mapper.readValue(result.getResponse().getContentAsString(), List.class);
        for(ProductDTO x : productDTOResponse){
            assertEquals(ProductType.CHEFCHOICE, x.getProductType());
        }
    }

    private MockMvc initMockMvc(){
        return MockMvcBuilders.standaloneSetup(productController).build();
    }
    private MockMvc initMockMvcMenuController(){
        return MockMvcBuilders.standaloneSetup(menuController).build();
    }

}