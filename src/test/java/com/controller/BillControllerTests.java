package com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.model.dto.*;
import com.model.entity.*;
import com.model.enums.BillStatus;
import com.model.enums.MenuType;
import com.repository.*;
import com.service.MenuCreationService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
import static org.mockito.ArgumentMatchers.any;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.codehaus.groovy.runtime.DefaultGroovyMethods.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// TODO: all test should include assert arrange act as comments so its easier to understand code
@SpringBootTest
class BillControllerTests {
    @Autowired
    private BillController billController;

    @Autowired
    MenuCreationService menuCreationService;

    @MockBean
    BillRepository billRepository;

    @MockBean
    ProductRepository productRepository;

    @MockBean
    GuestRepository guestRepository;

    @MockBean
    RestaurantRepository restaurantRepository;

    @MockBean
    RestaurentTableRepository restaurentTableRepository;

    @Captor
    ArgumentCaptor<RestaurentTable> restaurentTableArgumentCaptor;

    @Test
    void contextLoads() {
    }

    @Test
    public void testCreateMakeOrderByGuest() throws Exception {
        MockMvc mvc = initMockMvc();

        BillDTO billDTO = initBillDTO();
        ObjectMapper objectMapper = new ObjectMapper();


        JSONObject sendObj = new JSONObject();
        sendObj.put("billDTO", objectMapper.writeValueAsString(billDTO));
        sendObj.put("guestUsername", "guest@mail.com");
        sendObj.put("restaurentTableId", "1");
        sendObj.put("productDTO", objectMapper.writeValueAsString(billDTO.getOrderItems().get(0).getProduct()));
        sendObj.put("commentaire", "po de bacon po de bacon po de bacon");
        mockRepos();

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/order/makeOrder").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        BillDTO reponse = mapper.readValue(result.getResponse().getContentAsString(), BillDTO.class);

        assertEquals(1, reponse.getOrderItems().get(0).getOption().size());
        assertEquals("po de bacon po de bacon po de bacon", reponse.getOrderItems().get(0).getCommentaires());
        assertEquals(1, reponse.getOrderItems().get(0).getOption().get(0).getCheckItemList().size());
        assertTrue(reponse.getOrderItems().get(0).getOption().get(0).getCheckItemList().get(0).isActive());
        assertEquals(29.99, reponse.getPrixTotal());
        assertEquals("le steak chico", reponse.getOrderItems().get(0).getProduct().getName());
        assertEquals(1, reponse.getOrderItems().size());
        assertEquals("guest@mail.com", reponse.getOrderCustomer().getUsername());
    }

    @Test
    public void testCreateMakeOrderWithOrderItemCheckItemPlus() throws Exception {
        MockMvc mvc = initMockMvc();

        BillDTO billDTO = initBillDTOCheckItemPlusPrice();
        ObjectMapper objectMapper = new ObjectMapper();


        JSONObject sendObj = new JSONObject();
        sendObj.put("billDTO", objectMapper.writeValueAsString(billDTO));
        sendObj.put("guestUsername", "guest@mail.com");
        sendObj.put("restaurentTableId", "1");
        sendObj.put("productDTO", objectMapper.writeValueAsString(billDTO.getOrderItems().get(0).getProduct()));
        sendObj.put("commentaire", "po de bacon po de bacon po de bacon");

        mockRepos();

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/order/makeOrder").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        BillDTO reponse = mapper.readValue(result.getResponse().getContentAsString(), BillDTO.class);

        assertEquals(1, reponse.getOrderItems().get(0).getOption().size());
        assertEquals("po de bacon po de bacon po de bacon", reponse.getOrderItems().get(0).getCommentaires());
        assertEquals(1, reponse.getOrderItems().get(0).getOption().get(0).getCheckItemList().size());
        assertTrue(reponse.getOrderItems().get(0).getOption().get(0).getCheckItemList().get(0).isActive());
        assertEquals(34.99, reponse.getPrixTotal());
        assertEquals("le steak chico", reponse.getOrderItems().get(0).getProduct().getName());
        assertEquals(1, reponse.getOrderItems().size());
        assertEquals("guest@mail.com", reponse.getOrderCustomer().getUsername());
    }

    @Test
    public void testCreateMakeOrderWithCheckItemAddOn() throws Exception {
        MockMvc mvc = initMockMvc();

        BillDTO billDTO = initBillDTOProductCheckItemActive();
        ObjectMapper objectMapper = new ObjectMapper();


        JSONObject sendObj = new JSONObject();
        sendObj.put("billDTO", objectMapper.writeValueAsString(billDTO));
        sendObj.put("guestUsername", "guest@mail.com");
        sendObj.put("restaurentTableId", "1");
        sendObj.put("productDTO", objectMapper.writeValueAsString(billDTO.getOrderItems().get(0).getProduct()));
        sendObj.put("commentaire", "po de bacon po de bacon po de bacon");

        mockRepos();

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/order/makeOrder").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        BillDTO reponse = mapper.readValue(result.getResponse().getContentAsString(), BillDTO.class);

        assertEquals(1, reponse.getOrderItems().get(0).getOption().size());
        assertEquals("po de bacon po de bacon po de bacon", reponse.getOrderItems().get(0).getCommentaires());
        assertEquals(1, reponse.getOrderItems().get(0).getOption().get(0).getCheckItemList().size());
        assertTrue(reponse.getOrderItems().get(0).getOption().get(0).getCheckItemList().get(0).isActive());
        assertEquals(32.49, reponse.getPrixTotal());
        assertEquals("le steak chico", reponse.getOrderItems().get(0).getProduct().getName());
        assertEquals(1, reponse.getOrderItems().size());
        assertEquals("guest@mail.com", reponse.getOrderCustomer().getUsername());
    }

    @Test
    public void initVoid() throws Exception {
        MockMvc mvc = initMockMvc();


        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/order/initBill").
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        BillDTO response = mapper.readValue(result.getResponse().getContentAsString(), BillDTO.class);
        assertNotNull(response);
    }

    @Test
    public void fetchBillByIdGetRightBill() throws Exception {
        MockMvc mvc = initMockMvc();


        JSONObject sendObj = new JSONObject();
        sendObj.put("billId", "1");

        mockRepos();
        Bill bill = new Bill();
        bill.setOrderItems(new ArrayList<>());

        Mockito.when(billRepository.findById(any(Long.class))).thenReturn(Optional.of(bill));

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/order/getBill").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        BillDTO response = mapper.readValue(result.getResponse().getContentAsString(), BillDTO.class);
        assertNotNull( response);

    }

    @Test
    public void findBillStatus() throws Exception {
        // Arrange
        MockMvc mvc = initMockMvc();
        long billId = 1L;

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        mockRepos();

        Bill bill = new Bill();
        bill.setOrderItems(new ArrayList<>());
        bill.setBillStatus(BillStatus.PROGRESS);

        Mockito.when(billRepository.findById(any(Long.class))).thenReturn(Optional.of(bill));

        // Act
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/order/billStatus/" + billId).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();

        String status = mapper.readValue(result.getResponse().getContentAsString(), String.class);

        // Assert
        assertEquals(BillStatus.PROGRESS.toString(), status);
    }

    @Test
    public void testCreateMakeOrderAddItemToBillByGuest() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();

        BillDTO billDTO = initBillDTO();
        billDTO.setId(1L);
        ObjectMapper objectMapper = new ObjectMapper();

        mockRepos();
        Bill bill = new Bill();
        bill.setPrixTotal(29.99);
        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(menuCreationService.createProduct(MenuType.FOOD, "download.jpg", 29.99 , 30, "le steak chico" , null));
        orderItem.setOption(new ArrayList<>());
        orderItems.add(orderItem);
        bill.setOrderItems(orderItems);
        Mockito.when(billRepository.findById(any(Long.class))).thenReturn(Optional.of(bill));


        JSONObject sendObj = new JSONObject();
        sendObj.put("billDTO", objectMapper.writeValueAsString(billDTO));
        sendObj.put("guestUsername", "guest@mail.com");
        sendObj.put("restaurentTableId", "1");
        sendObj.put("productDTO", objectMapper.writeValueAsString(billDTO.getOrderItems().get(0).getProduct()));
        sendObj.put("commentaire", "po de bacon po de bacon po de bacon");

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/order/makeOrder").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        BillDTO reponse = mapper.readValue(result.getResponse().getContentAsString(), BillDTO.class);

        assertEquals(59.98, reponse.getPrixTotal());
    }


   /* @Test
    public void testCreateMakeOrderMultipleItemByGuest() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();

        RestaurantDTO restaurantDTO = new RestaurantDTO();
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setRestaurant(restaurantDTO);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(4);
        productDTO.setMenu(menuDTO);
        List<OrderItemDTO> orderItemDTOList = new ArrayList<>();
        OrderItemDTO orderItemDTO1 = new OrderItemDTO();
        orderItemDTO1.setProduct(productDTO);
        orderItemDTO1.setPrix(29.99);
        orderItemDTOList.add(orderItemDTO1);
        OrderItemDTO orderItemDTO2 = new OrderItemDTO();
        orderItemDTO2.setProduct(productDTO);
        orderItemDTO2.setPrix(29.99);
        orderItemDTOList.add(orderItemDTO2);
        BillDTO billDTO = new BillDTO();
        billDTO.setRestaurant(restaurantDTO);
        billDTO.setOrderItems(orderItemDTOList);


        ObjectMapper objectMapper = new ObjectMapper();


        JSONObject sendObj = new JSONObject();
        sendObj.put("billDTO", objectMapper.writeValueAsString(billDTO));
        sendObj.put("guestUsername", "guest@mail.com");
        sendObj.put("restaurentTableId", "1");
        sendObj.put("productDTO", objectMapper.writeValueAsString(billDTO.getOrderItems().get(0).getProduct()));


        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/order/makeOrder").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        sendObj = new JSONObject();
        sendObj.put("billDTO", objectMapper.writeValueAsString(objectMapper.readValue(result.getResponse().getContentAsString(), BillDTO.class)));
        sendObj.put("guestUsername", "guest@mail.com");
        sendObj.put("restaurentTableId", "1");
        sendObj.put("productDTO", objectMapper.writeValueAsString(billDTO.getOrderItems().get(1).getProduct()));


        result = mvc.perform(MockMvcRequestBuilders.post("/order/makeOrder").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        objectMapper.registerModule(new JavaTimeModule());
        BillDTO reponse = objectMapper.readValue(result.getResponse().getContentAsString(), BillDTO.class);

        assertEquals(1,reponse.getOrderItems().get(0).getOption().size());
        assertEquals(1,reponse.getOrderItems().get(0).getOption().get(0).getCheckItemList().size());
        assertTrue(reponse.getOrderItems().get(0).getOption().get(0).getCheckItemList().get(0).isActive());
        assertEquals(59.98, reponse.getPrixTotal());
        assertEquals("le steak chico", reponse.getOrderItems().get(0).getProduct().getName());
        assertEquals(2, reponse.getOrderItems().size());
        assertEquals("guest@mail.com", reponse.getOrderCustomer().getUsername());
    }*/

    @Test
    public void testCreateMakeOrderByClient() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();

        BillDTO billDTO = initBillDTO();
        ObjectMapper objectMapper = new ObjectMapper();


        JSONObject sendObj = new JSONObject();
        sendObj.put("billDTO", objectMapper.writeValueAsString(billDTO));
        sendObj.put("guestUsername", "client@mail.com");
        sendObj.put("restaurentTableId", "1");
        sendObj.put("productDTO", objectMapper.writeValueAsString(billDTO.getOrderItems().get(0).getProduct()));
        sendObj.put("commentaire", "po de bacon po de bacon po de bacon");

        mockRepos();
        Client client = new Client();
        client.setUsername("client@mail.com");
        Mockito.when(guestRepository.findByUsername(anyString())).thenReturn(Optional.of(client));

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/order/makeOrder").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();

        objectMapper.registerModule(new JavaTimeModule());
        BillDTO reponse = objectMapper.readValue(result.getResponse().getContentAsString(), BillDTO.class);

        assertEquals(1, reponse.getOrderItems().get(0).getOption().size());
        assertEquals("po de bacon po de bacon po de bacon", reponse.getOrderItems().get(0).getCommentaires());
        assertEquals(1, reponse.getOrderItems().get(0).getOption().get(0).getCheckItemList().size());
        assertTrue(reponse.getOrderItems().get(0).getOption().get(0).getCheckItemList().get(0).isActive());
        assertEquals(29.99, reponse.getPrixTotal());
        assertEquals("le steak chico", reponse.getOrderItems().get(0).getProduct().getName());
        assertEquals(1, reponse.getOrderItems().size());
        assertEquals("client@mail.com", reponse.getOrderCustomer().getUsername());
    }

    @Test
    public void testCreateMakePaymentReturnTrue() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();

        BillDTO billDTO = initBillDTO();
        ObjectMapper objectMapper = new ObjectMapper();


        JSONObject sendObj = new JSONObject();
        mockRepos();
        Bill bill = new Bill();
        RestaurentTable restaurentTable = new RestaurentTable();
        restaurentTable.setBills(new ArrayList<>());
        restaurentTable.getBills().add(bill);
        bill.setRestaurentTable(restaurentTable);

        Mockito.when(restaurentTableRepository.save(restaurentTableArgumentCaptor.capture())).thenReturn(null);


        Mockito.when(billRepository.findById(any(Long.class))).thenReturn(Optional.of(bill));


        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        sendObj = new JSONObject();
        sendObj.put("billId", 1L);
        MvcResult result =  mvc.perform(MockMvcRequestBuilders.post("/order/makePayment").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();

        assertTrue(mapper.readValue(result.getResponse().getContentAsString(), Boolean.class));
        assertEquals(0,restaurentTableArgumentCaptor.getValue().getBills().size());
    }


    private BillDTO initBillDTO() {
        RestaurantDTO restaurantDTO = new RestaurantDTO();
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setRestaurant(restaurantDTO);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(4);
        productDTO.setMenu(menuDTO);
        productDTO.setOptions(new ArrayList<>());
        OptionDTO optionDTO = new OptionDTO();
        optionDTO.setName("cuisson");
        optionDTO.setCheckItemList(new ArrayList<>());
        CheckItemDTO checkItemDTO = new CheckItemDTO();
        checkItemDTO.setName("medium");
        checkItemDTO.setActive(true);
        productDTO.setCheckItems(new ArrayList<>());
        CheckItemDTO productCheckItem = new CheckItemDTO();
        productCheckItem.setName("fromage");
        productCheckItem.setPrix(2.50);
        productDTO.getCheckItems().add(productCheckItem);
        optionDTO.getCheckItemList().add(checkItemDTO);
        productDTO.getOptions().add(optionDTO);
        OrderItemDTO orderItemDTO1 = new OrderItemDTO();
        orderItemDTO1.setProduct(productDTO);
        orderItemDTO1.setPrix(29.99);
        orderItemDTO1.setOption(new ArrayList<>());
        orderItemDTO1.getOption().add(optionDTO);
        List<OrderItemDTO> orderItemDTOList = new ArrayList<>();
        orderItemDTOList.add(orderItemDTO1);
        BillDTO billDTO = new BillDTO();
        billDTO.setRestaurant(restaurantDTO);
        billDTO.setOrderItems(orderItemDTOList);
        return billDTO;
    }

    private BillDTO initBillDTOCheckItemPlusPrice() {
        BillDTO billDTO = initBillDTO();
        CheckItemDTO checkItemDTO = new CheckItemDTO();
        checkItemDTO.setName("medium");
        checkItemDTO.setPrix(5.00);
        checkItemDTO.setActive(true);
        billDTO.getOrderItems().get(0).getOption().get(0).setCheckItemList(new ArrayList<>());
        billDTO.getOrderItems().get(0).getOption().get(0).getCheckItemList().add(checkItemDTO);

        return billDTO;
    }

    private BillDTO initBillDTOProductCheckItemActive() {
        BillDTO billDTO = initBillDTO();
        billDTO.getOrderItems().get(0).getProduct().getCheckItems().get(0).setActive(true);
        return billDTO;
    }

    private void mockRepos() throws IOException {
        Menu menu = new Menu();
        menu.setMenuType(MenuType.FOOD);
        menu.setProducts(new ArrayList<>());
        RestaurentTable restaurentTable = new RestaurentTable();
        restaurentTable.setBills(new ArrayList<>());
        restaurentTable.setRestaurant(new Restaurant());
        Guest guest= new Guest();
        guest.setUsername("guest@mail.com");
        Mockito.when(restaurentTableRepository.findById(any(Long.class))).thenReturn(Optional.of(restaurentTable));
        Mockito.when(guestRepository.findByUsername(anyString())).thenReturn(Optional.of(guest));
        Mockito.when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(  menuCreationService.createProduct(MenuType.FOOD, "download.jpg", 29.99 , 30, "le steak chico" , menu)));
        Mockito.when(billRepository.save(any(Bill.class))).thenAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                return (Bill) invocation.getArguments()[0];
            }
        });
    }

    private MockMvc initMockMvc() {
        return MockMvcBuilders.standaloneSetup(billController).build();
    }

}
