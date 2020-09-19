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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("dev")
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
        Product reponse = mapper.readValue(result.getResponse().getContentAsString(), Product.class);
        assertEquals(ProductType.SPECIAL, reponse.getProductType());

        JSONObject sendObj2 = new JSONObject();
        sendObj2.put("menuId","1");
        mvc = initMockMvcMenuController();
        result= mvc.perform(MockMvcRequestBuilders.post(   "/menu/getMenu").
                content(sendObj2.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        MenuDTO menuDTOResponse = mapper.readValue(result.getResponse().getContentAsString(), MenuDTO.class);

        assertEquals(ProductType.SPECIAL, menuDTOResponse.getProducts().get(0).getProductType());
    }

    private ProductDTO initProductDTO() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setPrix(39.99);
        productDTO.setName("killua");
        return productDTO;
    }

    @Test
    public void testSetSpecialPourProduitChercherTypeProduitNonModifie() throws Exception {
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
        Product reponse = mapper.readValue(result.getResponse().getContentAsString(), Product.class);
        assertEquals(ProductType.SPECIAL, reponse.getProductType());

        JSONObject sendObj2 = new JSONObject();
        sendObj2.put("menuId","1");
        mvc = initMockMvcMenuController();
        result= mvc.perform(MockMvcRequestBuilders.post(   "/menu/getMenu").
                content(sendObj2.toString()).
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
        Product reponse = mapper.readValue(result.getResponse().getContentAsString(), Product.class);
        assertEquals(ProductType.CHEFCHOICE, reponse.getProductType());

        JSONObject sendObj2 = new JSONObject();
        sendObj2.put("menuId","1");
        mvc = initMockMvcMenuController();
        result= mvc.perform(MockMvcRequestBuilders.post(   "/menu/getMenu").
                content(sendObj2.toString()).
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
        Product reponse = mapper.readValue(result.getResponse().getContentAsString(), Product.class);
        assertEquals(ProductType.SPECIAL, reponse.getProductType());

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
        Product reponse = mapper.readValue(result.getResponse().getContentAsString(), Product.class);
        assertEquals(ProductType.SPECIAL, reponse.getProductType());

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
            assertEquals(ProductType.SPECIAL, x.getProductType());
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
        Product reponse = mapper.readValue(result.getResponse().getContentAsString(), Product.class);
        assertEquals(ProductType.CHEFCHOICE, reponse.getProductType());

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
            assertEquals(ProductType.CHEFCHOICE, x.getProductType());
        }
    }

    @Test
    public void testCreateProductImage() throws Exception {
        MockMvc mvc = initMockMvc();
        JSONObject productDTO= new JSONObject();
        productDTO.put("id","1");
        MockMultipartFile multipartFile = new MockMultipartFile("file", "img.png",
                "image/jpg", "Spring Framework".getBytes());
        MockMultipartFile employee = new MockMultipartFile("productDTO", "",
                "application/json", productDTO.toString().getBytes());
        MvcResult result=mvc.perform(multipart("/product/saveProductImg").file(multipartFile).file(employee))
                .andExpect(status().isOk())
                .andReturn();
        Path currentRelativePath = Paths.get("");
        String absolutePath = currentRelativePath.toAbsolutePath().toString();
        JSONObject returnValue = new JSONObject(result.getResponse().getContentAsString());
        ProductDTO reponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(), ProductDTO.class);
        File file =new File(absolutePath+reponse.getImgUrl());
        assertTrue(file.exists());

    }
    private MockMvc initMockMvc(){
        return MockMvcBuilders.standaloneSetup(productController).build();
    }
    private MockMvc initMockMvcMenuController(){
        return MockMvcBuilders.standaloneSetup(menuController).build();
    }

}