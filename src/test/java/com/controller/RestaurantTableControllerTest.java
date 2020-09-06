package com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.model.dto.*;
import com.model.entity.Product;
import com.model.enums.ProductType;
import com.service.ClientService;
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
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class RestaurantTableControllerTest {
    @Autowired
    RestaurantTableController restaurantTableController;

    @Autowired
    MenuController menuController;

    @Autowired
    BillController billController;

    @Autowired
    ClientService clientService;
    @Test
    public void testFetchRestaurantSpecial() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String,String> requestParams = new LinkedMultiValueMap<>();

        BillDTO billDTO = initBillDTO();
        ObjectMapper objectMapper =new ObjectMapper();


        JSONObject sendObj = new JSONObject();
        sendObj.put("bill",objectMapper.writeValueAsString(billDTO));
        sendObj.put("guestUsername","client1");
        sendObj.put("restaurentId","1");

        MvcResult result= mvc.perform(MockMvcRequestBuilders.post(   "/restaurantTable/createRate").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ObjectMapper mapper=new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        List<RestaurentTableDTO> reponse = mapper.readValue(result.getResponse().getContentAsString(),List.class);
        assertEquals(1, reponse.size());
    }

    @Test
    public void testBillRetirer() throws Exception {
        MockMvc mvc = initMockMvcBillController();
        LinkedMultiValueMap<String,String> requestParams = new LinkedMultiValueMap<>();


        BillDTO billDTO = initBillDTO();
        ObjectMapper objectMapper =new ObjectMapper();


        JSONObject sendObj = new JSONObject();
        sendObj.put("bill",objectMapper.writeValueAsString(billDTO));
        sendObj.put("guestUsername","client1");
        sendObj.put("restaurentTableId","5");

        MvcResult result= mvc.perform(MockMvcRequestBuilders.post(   "/order/makeOrder").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();

        ObjectMapper mapper=new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());


        BillDTO responseBill=mapper.readValue(result.getResponse().getContentAsString(),BillDTO.class);
        clientService.makePayment(responseBill.getId());

        sendObj = new JSONObject();
        sendObj.put("restaurentId",1);
        mvc = initMockMvc();
        result= mvc.perform(MockMvcRequestBuilders.post(   "/restaurantTable/createRate").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        List<LinkedHashMap<String,Object>> reponse = mapper.readValue(result.getResponse().getContentAsString(),ArrayList.class);

        List<BillDTO> billDTOS = mapper.readValue(reponse.get(0).get("billDTOList").toString(),ArrayList.class);
       // billDTOList
       // List<BillDTO> billDTOS =reponse.get("billDTOList");
        assertEquals(0, billDTOS.size());
    }

    private MockMvc initMockMvc(){
        return MockMvcBuilders.standaloneSetup(restaurantTableController).build();
    }
    private MockMvc initMockMvcBillController(){
        return MockMvcBuilders.standaloneSetup(billController).build();
    }
    private BillDTO initBillDTO() {
        RestaurantDTO restaurantDTO = new RestaurantDTO();
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setRestaurant(restaurantDTO);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(3);
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


}