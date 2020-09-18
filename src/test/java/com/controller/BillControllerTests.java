package com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.model.dto.*;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BillControllerTests {
    @Autowired
    BillController billController;

    @Test
    void contextLoads() {
    }

    @Test
    public void testCreateMakeOrderByGuest() throws Exception {
        MockMvc mvc = initMockMvc();

        BillDTO billDTO = initBillDTO();
        ObjectMapper objectMapper = new ObjectMapper();


        JSONObject sendObj = new JSONObject();
        sendObj.put("bill",objectMapper.writeValueAsString(billDTO));
        sendObj.put("guestUsername","guest@mail.com");
        sendObj.put("restaurentTableId","6");

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/order/makeOrder").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        BillDTO reponse = mapper.readValue(result.getResponse().getContentAsString(), BillDTO.class);
        assertEquals(29.99, reponse.getPrixTotal());
        assertEquals("le steak chico", reponse.getOrderItems().get(0).getProduct().getName());
        assertEquals(1, reponse.getOrderItems().size());
        assertEquals("guest@mail.com", reponse.getOrderCustomer().getUsername());
    }

    @Test
    public void testCreateMakeOrderAddItemToBillByGuest() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();

        BillDTO billDTO = initBillDTO();
        ObjectMapper objectMapper = new ObjectMapper();


        JSONObject sendObj = new JSONObject();
        sendObj.put("bill",objectMapper.writeValueAsString(billDTO));
        sendObj.put("guestUsername","guest@mail.com");
        sendObj.put("restaurentTableId","6");

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/order/makeOrder").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ObjectMapper mapper=new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        BillDTO reponse = mapper.readValue(result.getResponse().getContentAsString(),BillDTO.class);

        billDTO = initBillDTO();
        billDTO.setId(reponse.getId());
        objectMapper = new ObjectMapper();


        sendObj = new JSONObject();
        sendObj.put("bill",objectMapper.writeValueAsString(billDTO));
        sendObj.put("guestUsername","guest@mail.com");
        sendObj.put("restaurentTableId","6");
        result= mvc.perform(MockMvcRequestBuilders.post(   "/order/makeOrder").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        objectMapper.registerModule(new JavaTimeModule());
        reponse = objectMapper.readValue(result.getResponse().getContentAsString(),BillDTO.class);
        assertEquals(59.98, reponse.getPrixTotal());
        assertEquals("le steak chico", reponse.getOrderItems().get(0).getProduct().getName());
        assertEquals(2, reponse.getOrderItems().size());
        assertEquals("guest@mail.com", reponse.getOrderCustomer().getUsername());
    }

    @Test
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
        sendObj.put("bill",objectMapper.writeValueAsString(billDTO));
        sendObj.put("guestUsername","guest@mail.com");
        sendObj.put("restaurentTableId","6");

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/order/makeOrder").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        objectMapper.registerModule(new JavaTimeModule());
        BillDTO reponse = objectMapper.readValue(result.getResponse().getContentAsString(),BillDTO.class);
        assertEquals(59.98, reponse.getPrixTotal());
        assertEquals("le steak chico", reponse.getOrderItems().get(0).getProduct().getName());
        assertEquals(2, reponse.getOrderItems().size());
        assertEquals("guest@mail.com", reponse.getOrderCustomer().getUsername());
    }


    @Test
    public void testCreateMakeOrderByClient() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();

        BillDTO billDTO = initBillDTO();
        ObjectMapper objectMapper = new ObjectMapper();


        JSONObject sendObj = new JSONObject();
        sendObj.put("bill",objectMapper.writeValueAsString(billDTO));
        sendObj.put("guestUsername","client1@mail.com");
        sendObj.put("restaurentTableId","6");

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/order/makeOrder").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();

        objectMapper.registerModule(new JavaTimeModule());
        BillDTO reponse = objectMapper.readValue(result.getResponse().getContentAsString(),BillDTO.class);
        assertEquals(29.99, reponse.getPrixTotal());
        assertEquals("le steak chico", reponse.getOrderItems().get(0).getProduct().getName());
        assertEquals(1, reponse.getOrderItems().size());
        assertEquals("client1@mail.com", reponse.getOrderCustomer().getUsername());
    }
    @Test
    public void testCreateMakePaymentReturnTrue() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();

        BillDTO billDTO = initBillDTO();
        ObjectMapper objectMapper = new ObjectMapper();


        JSONObject sendObj = new JSONObject();
        sendObj.put("bill",objectMapper.writeValueAsString(billDTO));
        sendObj.put("guestUsername","guest@mail.com");
        sendObj.put("restaurentTableId","6");

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
        sendObj.put("billId",reponse.getId());
        sendObj.put("bill",objectMapper.writeValueAsString(billDTO));
        sendObj.put("guestUsername","guest@mail.com");
        sendObj.put("restaurentTableId","6");
        result =mvc.perform(MockMvcRequestBuilders.post("/order/makePayment").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();

        assertTrue(mapper.readValue(result.getResponse().getContentAsString(), Boolean.class));
    }


    private BillDTO initBillDTO() {
        RestaurantDTO restaurantDTO = new RestaurantDTO();
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setRestaurant(restaurantDTO);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(4);
        productDTO.setMenu(menuDTO);
        OrderItemDTO orderItemDTO1 = new OrderItemDTO();
        orderItemDTO1.setProduct(productDTO);
        orderItemDTO1.setPrix(29.99);
        List<OrderItemDTO> orderItemDTOList = new ArrayList<>();
        orderItemDTOList.add(orderItemDTO1);
        BillDTO billDTO = new BillDTO();
        billDTO.setRestaurant(restaurantDTO);
        billDTO.setOrderItems(orderItemDTOList);
        return billDTO;
    }

    private MockMvc initMockMvc() {
        return MockMvcBuilders.standaloneSetup(billController).build();
    }

}
