package com.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.model.dto.*;
import com.model.dto.requests.FindBillBetweenDateRequestDTO;
import com.model.enums.BillStatus;
import com.repository.BillRepository;
import com.repository.RestaurantRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// TODO: all test should include assert arrange act as comments so its easier to understand code
@SpringBootTest
class BillControllerTests {
    @Autowired
    private BillController billController;

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
        assertEquals(BigDecimal.valueOf(29.99), reponse.getPrix());
        assertEquals(BigDecimal.valueOf(4.50).doubleValue(), reponse.getTips().doubleValue());
        assertEquals(BigDecimal.valueOf(34.49), reponse.getPrixTotal());
        assertEquals("Steak chico dejeuner 0", reponse.getOrderItems().get(0).getProduct().getName());
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
        assertEquals(BigDecimal.valueOf(40.24), reponse.getPrixTotal());
        assertEquals("Steak chico dejeuner 0", reponse.getOrderItems().get(0).getProduct().getName());
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
        assertEquals(BigDecimal.valueOf(37.37), reponse.getPrixTotal());
        assertEquals("Steak chico dejeuner 0", reponse.getOrderItems().get(0).getProduct().getName());
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
        assertNotNull(response.getId());
    }

    @Test
    public void fetchBillByIdGetRightBill() throws Exception {
        MockMvc mvc = initMockMvc();

        JSONObject sendObj = new JSONObject();
        sendObj.put("billId", "1");
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/order/getBill").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        BillDTO response = mapper.readValue(result.getResponse().getContentAsString(), BillDTO.class);
        assertEquals(1, response.getId());
    }

    @Test
    public void findBillStatus() throws Exception {
        // Arrange
        MockMvc mvc = initMockMvc();
        long billId = 1L;

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

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
        ObjectMapper objectMapper = new ObjectMapper();


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

        billDTO = initBillDTO();
        billDTO.setId(reponse.getId());
        objectMapper = new ObjectMapper();


        sendObj = new JSONObject();
        sendObj.put("billDTO", objectMapper.writeValueAsString(billDTO));
        sendObj.put("guestUsername", "guest@mail.com");
        sendObj.put("restaurentTableId", "1");
        sendObj.put("commentaire", "po de bacon po de bacon po de bacon");
        sendObj.put("productDTO", objectMapper.writeValueAsString(billDTO.getOrderItems().get(0).getProduct()));

        result = mvc.perform(MockMvcRequestBuilders.post("/order/makeOrder").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        objectMapper.registerModule(new JavaTimeModule());
        reponse = objectMapper.readValue(result.getResponse().getContentAsString(), BillDTO.class);

        assertEquals(1, reponse.getOrderItems().get(0).getOption().size());
        assertEquals("po de bacon po de bacon po de bacon", reponse.getOrderItems().get(0).getCommentaires());
        assertEquals(1, reponse.getOrderItems().get(0).getOption().get(0).getCheckItemList().size());
        assertTrue(reponse.getOrderItems().get(0).getOption().get(0).getCheckItemList().get(0).isActive());
        assertEquals(BigDecimal.valueOf(68.98), reponse.getPrixTotal());
        assertEquals("Steak chico dejeuner 0", reponse.getOrderItems().get(0).getProduct().getName());
        assertEquals(2, reponse.getOrderItems().size());
        assertEquals("guest@mail.com", reponse.getOrderCustomer().getUsername());
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
        assertEquals(BigDecimal.valueOf(34.49), reponse.getPrixTotal());
        assertEquals("Steak chico dejeuner 0", reponse.getOrderItems().get(0).getProduct().getName());
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
        sendObj = new JSONObject();
        sendObj.put("billId", reponse.getId());
        sendObj.put("billDTO", objectMapper.writeValueAsString(billDTO));
        sendObj.put("guestUsername", "guest@mail.com");
        sendObj.put("restaurentTableId", "1");
        result = mvc.perform(MockMvcRequestBuilders.post("/order/makePayment").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();

        assertTrue(mapper.readValue(result.getResponse().getContentAsString(), Boolean.class));
    }

    @Test
    public void testUpdateBill() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        String terminalRequestStatus = "TERMINALREQUESTWATING";
        Double billTipsAdded = 5.25;

        JSONObject sendObj = new JSONObject();
        sendObj.put("id", 2L);
        sendObj.put("billStatus", terminalRequestStatus);
        sendObj.put("tips", billTipsAdded);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.put("/order/updateBills").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        BillDTO reponse = mapper.readValue(result.getResponse().getContentAsString(), BillDTO.class);

        assertEquals(BillStatus.TERMINALREQUESTWATING, reponse.getBillStatus());
        assertEquals(BigDecimal.valueOf(billTipsAdded), reponse.getTips());
        assertEquals(BigDecimal.valueOf(billTipsAdded+10), reponse.getPrixTotal());

    }

    @Test
    public void findAllPaidBillsByRestaurantBetweenDatesTest() throws Exception {
        // Arrange
        MockMvc mvc = initMockMvc();

        FindBillBetweenDateRequestDTO requestDTO = new FindBillBetweenDateRequestDTO();
        requestDTO.setBegin(LocalDateTime.of(2020, 12, 12, 12, 12));
        requestDTO.setEnd(LocalDateTime.now());
        requestDTO.setRestaurantId(2L);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // Act

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/order/getPaidBillsBetweenDates").
                content(mapper.writeValueAsString(requestDTO)).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();

        List<BillDTO> billDTOList = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<BillDTO>>() {});

        billDTOList.forEach(billDTO -> {
            assertTrue(billDTO.getDate().isAfter(requestDTO.getBegin()));
            assertTrue(billDTO.getDate().isBefore(requestDTO.getEnd()));
        });
    }


    private BillDTO initBillDTO() {
        RestaurantDTO restaurantDTO = new RestaurantDTO();
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setRestaurant(restaurantDTO);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(8);
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
        productCheckItem.setPrix(BigDecimal.valueOf(2.50));
        productDTO.getCheckItems().add(productCheckItem);
        optionDTO.getCheckItemList().add(checkItemDTO);
        productDTO.getOptions().add(optionDTO);
        OrderItemDTO orderItemDTO1 = new OrderItemDTO();
        orderItemDTO1.setProduct(productDTO);
        orderItemDTO1.setPrix(BigDecimal.valueOf(29.99));
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
        checkItemDTO.setPrix(BigDecimal.valueOf(5.00));
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

    private MockMvc initMockMvc() {
        return MockMvcBuilders.standaloneSetup(billController).build();
    }

}
