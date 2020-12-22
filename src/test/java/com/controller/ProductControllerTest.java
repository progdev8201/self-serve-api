package com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.model.dto.*;
import com.model.entity.*;
import com.model.enums.MenuType;
import com.repository.MenuRepository;
import com.repository.ProductRepository;
import com.repository.RestaurantRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

    @MockBean
    ProductRepository productRepository;

    @MockBean
    MenuRepository menuRepository;

    @MockBean
    RestaurantRepository restaurantRepository;

    @Test
    public void findAllProductFromMenu() throws Exception {
        MockMvc mvc = initMockMvc();
        Mockito.when(menuRepository.findById(any(Long.class))).thenReturn(Optional.of(initMenuObj()));
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/product/menu/{id}", "1").
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        List<ProductDTO> productDTOS = mapper.readValue(result.getResponse().getContentAsString(), List.class);
        assertEquals(5, productDTOS.size());
    }

    @Test
    public void findProductById() throws Exception {
        MockMvc mvc = initMockMvc();

        Product product = new Product();
        product.setImgFile(new ImgFile());
        product.setCheckItems(new ArrayList<>());
        product.getCheckItems().add(new CheckItem());
        Option option = new Option();
        option.setCheckItemList(new ArrayList<>());
        product.setOptions( new ArrayList<>());
        product.getOptions().add(option);

        Mockito.when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/product/{id}", "1").
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        ProductDTO productDTO = mapper.readValue(result.getResponse().getContentAsString(), ProductDTO.class);
        assertNotNull(productDTO);
    }

    @Test
    public void testFetchMenuWaiterRequest() throws Exception {
        MockMvc mvc = initMockMvc();
        Menu menu = initMenuObj();
        Restaurant restaurant = new Restaurant();
        restaurant.setMenus(new ArrayList<>());
        restaurant.getMenus().add(menu);

        Mockito.when(restaurantRepository.findById(any(Long.class))).thenReturn(Optional.of(restaurant));
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/product/findWaiterRequestProducts/{id}", "2").
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        MenuDTO menuDTO = mapper.readValue(result.getResponse().getContentAsString(), MenuDTO.class);
        assertEquals(5, menuDTO.getProducts().size());
    }

    private Menu initMenuObj() {
        Menu menu = new Menu();
        menu.setMenuType(MenuType.WAITERREQUEST);
        menu.setProducts(new ArrayList<>());
        for(int i=0;i<5;i++){
            Product product = new Product();
            product.setOptions(new ArrayList<>());
            product.setCheckItems(new ArrayList<>());
            product.setMenuType(MenuType.WAITERREQUEST);
            menu.getProducts().add(product);
        }
        return menu;
    }

    @Test
    public void deleteProduct() throws Exception {
        MockMvc mvc = initMockMvc();

        Menu menu = new Menu();
        menu.setProducts(new ArrayList<>());
        Product product = new Product();
        product.setMenu(menu);
        product.setId(1L);
        product.setOrderItems(new ArrayList<>());
        menu.getProducts().add(product);


        Mockito.when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));

        MvcResult result = mvc.perform(MockMvcRequestBuilders.delete("/product/{id}", "1").
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();

    }

    @Test
    public void testCreateProduct() throws Exception {
        MockMvc mvc = initMockMvc();
        ProductDTO productDTO = initProductDTONoId();
        ObjectMapper objectMapper = new ObjectMapper();

        Menu menu = new Menu();
        menu.setProducts(new ArrayList<>());
        Product product = new Product();
        product.setMenu(menu);
        product.setId(1L);
        product.setOrderItems(new ArrayList<>());
        menu.getProducts().add(product);

        Mockito.when(menuRepository.findById(any(Long.class))).thenReturn(Optional.of(menu));

        Mockito.when(menuRepository.save(any(Menu.class))).thenAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                return (Menu) invocation.getArguments()[0];
            }
        });




        JSONObject sendObj = new JSONObject();
        sendObj.put("productDTO", objectMapper.writeValueAsString(productDTO));
        //on utilise un menu different pour pas que ca interfere avk les autres tests
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/product/{menuId}", "2").
                content(objectMapper.writeValueAsString(productDTO)).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ProductDTO returnValue = objectMapper.readValue(result.getResponse().getContentAsString(), ProductDTO.class);
        assertEquals("killua", returnValue.getName());
        assertEquals(39.99, returnValue.getPrix());
        assertEquals(1, productDTO.getCheckItems().size());
        assertEquals(1, productDTO.getOptions().size());

    }



    private MockMvc initMockMvc() {
        return MockMvcBuilders.standaloneSetup(productController).build();
    }

    private MockMvc initMockMvcMenuController() {
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