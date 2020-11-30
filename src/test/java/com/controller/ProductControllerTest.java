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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// TODO: all test should include assert arrange act as comments so its easier to understand code
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

        ProductDTO productDTO = initProductDTO();
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
        ProductDTO reponse = mapper.readValue(result.getResponse().getContentAsString(), ProductDTO.class);
        assertEquals(MenuType.SPECIAL, reponse.getProductType());

    }
    @Test
    public void findAllProductFromMenu() throws Exception {
        MockMvc mvc = initMockMvc();
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/product/menu/{id}","1").
                            contentType(MediaType.APPLICATION_JSON).
                            accept(MediaType.APPLICATION_JSON)).
                            andExpect(status().isOk()).
                            andReturn();
        ObjectMapper mapper=new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        List<ProductDTO> productDTOS = mapper.readValue(result.getResponse().getContentAsString(),List.class);
        assertEquals(3,productDTOS.size());
    }

    @Test
    public void findProductById() throws Exception {
        MockMvc mvc = initMockMvc();
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/product/{id}","1").
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ObjectMapper mapper=new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        ProductDTO productDTO = mapper.readValue(result.getResponse().getContentAsString(),ProductDTO.class);
        assertNotNull(productDTO);
    }

    @Test
    public void testFetchMenuWaiterRequest() throws Exception {
        MockMvc mvc = initMockMvc();
        MvcResult result= mvc.perform(MockMvcRequestBuilders.get(   "/product/findWaiterRequestProducts/{id}","2").
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ObjectMapper mapper=new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        MenuDTO menuDTO = mapper.readValue(result.getResponse().getContentAsString(),MenuDTO.class);
        assertEquals(6,menuDTO.getProducts().size());
    }

    @Test
    public void deleteProduct() throws Exception {
        MockMvc mvc = initMockMvc();

        MvcResult result= mvc.perform(MockMvcRequestBuilders.delete(   "/product/{id}","16").
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();

    }
    @Test
    public void testSetChoixChefPourProduit() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String,String> requestParams = new LinkedMultiValueMap<>();

        ProductDTO productDTO = initProductDTO();
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
        ProductDTO reponse = mapper.readValue(result.getResponse().getContentAsString(), ProductDTO.class);
        assertEquals(MenuType.CHEFCHOICE, reponse.getProductType());

    }

    @Test
    public void testCreateProduct() throws Exception {
        MockMvc mvc= initMockMvc();
        ProductDTO productDTO = initProductDTONoId();
        ObjectMapper objectMapper =new ObjectMapper();


        JSONObject sendObj = new JSONObject();
        sendObj.put("productDTO",objectMapper.writeValueAsString(productDTO));
        //on utilise un menu different pour pas que ca interfere avk les autres tests
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/product/{menuId}","2").
                            content(objectMapper.writeValueAsString(productDTO)).
                            contentType(MediaType.APPLICATION_JSON).
                            accept(MediaType.APPLICATION_JSON)).
                            andExpect(status().isOk()).
                            andReturn();
        ProductDTO returnValue = objectMapper.readValue(result.getResponse().getContentAsString(),ProductDTO.class);
        assertEquals("killua",returnValue.getName());
        assertEquals(39.99,returnValue.getPrix());
        assertEquals(1,productDTO.getCheckItems().size());
        assertEquals(1,productDTO.getOptions().size());

    }

    @Test
    public void testRemoveSpecialProduit() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String,String> requestParams = new LinkedMultiValueMap<>();

        ProductDTO productDTO = initProductDTO();
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
        ProductDTO reponse = mapper.readValue(result.getResponse().getContentAsString(), ProductDTO.class);
        assertEquals(MenuType.SPECIAL, reponse.getProductType());

         result= mvc.perform(MockMvcRequestBuilders.post(   "/product/deleteProductType").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ProductDTO productDTOResponse = mapper.readValue(result.getResponse().getContentAsString(), ProductDTO.class);
        assertEquals(productDTO.getId(), productDTOResponse.getId());
        assertEquals(null, productDTOResponse.getProductType());
    }

    @Test
    public void testGetListeSpecial() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String,String> requestParams = new LinkedMultiValueMap<>();

        ProductDTO productDTO = initProductDTO();
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
        productDTO.setId(2L);
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
        ProductDTO reponse = mapper.readValue(result.getResponse().getContentAsString(), ProductDTO.class);
        assertEquals(MenuType.SPECIAL, reponse.getProductType());

        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setId(1L);
        sendObj = new JSONObject();
        sendObj.put("menuDTO",objectMapper.writeValueAsString(menuDTO));

        result= mvc.perform(MockMvcRequestBuilders.get(   "/product/findMenuSpecial").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        List<ProductDTO> productDTOResponse = Arrays.stream(mapper.readValue(result.getResponse().getContentAsString(), ProductDTO[].class)).collect(Collectors.toList());
        for(ProductDTO x : productDTOResponse){
            assertEquals(MenuType.SPECIAL, x.getProductType());
        }
    }

    @Test
    public void testGetListeChoixChef() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String,String> requestParams = new LinkedMultiValueMap<>();

        ProductDTO productDTO = initProductDTO();
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
        productDTO.setId(2L);
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
        ProductDTO reponse = mapper.readValue(result.getResponse().getContentAsString(), ProductDTO.class);
        assertEquals(MenuType.CHEFCHOICE, reponse.getProductType());

        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setId(1L);
        sendObj = new JSONObject();
        sendObj.put("menuDTO",objectMapper.writeValueAsString(menuDTO));

        result= mvc.perform(MockMvcRequestBuilders.get(   "/product/findChoixDuChef").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        List<ProductDTO> productDTOResponse =Arrays.stream(mapper.readValue(result.getResponse().getContentAsString(), ProductDTO[].class)).collect(Collectors.toList());
        for(ProductDTO x : productDTOResponse){
            assertEquals(MenuType.CHEFCHOICE, x.getProductType());
        }
    }

    private MockMvc initMockMvc(){
        return MockMvcBuilders.standaloneSetup(productController).build();
    }

    private MockMvc initMockMvcMenuController(){
        return MockMvcBuilders.standaloneSetup(menuController).build();
    }

    private ProductDTO initProductDTO() {
        ProductDTO productDTO = initProductDTONoId();
        productDTO.setId(1L);

        return productDTO;
    }
    private ProductDTO initProductDTONoId() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setPrix(39.99);
        productDTO.setName("killua");
        productDTO.setCheckItems(new ArrayList<>());
        productDTO.getCheckItems().add(new CheckItemDTO());
        productDTO.setOptions(new ArrayList<>());
        OptionDTO option = new OptionDTO();
        option.setCheckItemList(new ArrayList<>());
        option.getCheckItemList().add(new CheckItemDTO());
        productDTO.getOptions().add(option);
        productDTO.setCheckItems(new ArrayList<>());
        productDTO.getCheckItems().add(new CheckItemDTO());
        return productDTO;
    }
}