package com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.model.dto.*;
import com.model.entity.OrderItem;
import com.model.enums.OrderStatus;
import com.service.ClientService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("dev")
class KitchenRestControllerTest {

    @Autowired
    KitchenRestController kitchenRestController;

    @Autowired
    MenuController menuController;

    @Autowired
    BillController billController;

    @Autowired
    ClientService clientService;
    @Test
    public void testFetchRestaurantTableBillFound() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String,String> requestParams = new LinkedMultiValueMap<>();

        BillDTO billDTO = initBillDTO();
        ObjectMapper objectMapper =new ObjectMapper();


        JSONObject sendObj = new JSONObject();
        sendObj.put("bill",objectMapper.writeValueAsString(billDTO));
        sendObj.put("guestUsername","client1@mail.com");
        sendObj.put("restaurentId","2");

        MvcResult result= mvc.perform(MockMvcRequestBuilders.post(   "/rest/kitchen/findAllTables").
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
    public void testBillRetirerRestaurantTableBillNull() throws Exception {
        MockMvc mvc = initMockMvcBillController();
        LinkedMultiValueMap<String,String> requestParams = new LinkedMultiValueMap<>();


        BillDTO billDTO = initBillDTO();
        ObjectMapper objectMapper =new ObjectMapper();


        JSONObject sendObj = new JSONObject();
        sendObj.put("billDTO",objectMapper.writeValueAsString(billDTO));
        sendObj.put("guestUsername","client1@mail.com");
        sendObj.put("restaurentTableId","1");
        sendObj.put("productDTO", objectMapper.writeValueAsString(billDTO.getOrderItems().get(0).getProduct()));

        MvcResult result= mvc.perform(MockMvcRequestBuilders.post(   "/order/makeOrder").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();

        ObjectMapper mapper=new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());


        BillDTO responseBill=mapper.readValue(result.getResponse().getContentAsString(),BillDTO.class);

        sendObj = new JSONObject();
        sendObj.put("restaurentId",2);
        mvc = initMockMvc();
        result= mvc.perform(MockMvcRequestBuilders.post(   "/rest/kitchen/findAllTables").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        List<LinkedHashMap<String,Object>> reponse = mapper.readValue(result.getResponse().getContentAsString(), ArrayList.class);

        ArrayList billDTOS = (ArrayList)reponse.get(0).get("billDTOList");
        // billDTOList
        // List<BillDTO> billDTOS =reponse.get("billDTOList");
        assertEquals(1, billDTOS.size());

        clientService.makePayment(responseBill.getId());

        sendObj = new JSONObject();
        sendObj.put("restaurentId",2);
        mvc = initMockMvc();
        result= mvc.perform(MockMvcRequestBuilders.post(   "/rest/kitchen/findAllTables").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        reponse = mapper.readValue(result.getResponse().getContentAsString(), ArrayList.class);

        billDTOS = (ArrayList)reponse.get(0).get("billDTOList");
        // billDTOList
        // List<BillDTO> billDTOS =reponse.get("billDTOList");
        assertEquals(0, billDTOS.size());
    }
    @Test
    public void testFetchRestaurantChangeOrderItemStatus() throws Exception {
        MockMvc mvc = initMockMvcBillController();
        LinkedMultiValueMap<String,String> requestParams = new LinkedMultiValueMap<>();


        BillDTO billDTO = initBillDTO();
        ObjectMapper objectMapper =new ObjectMapper();


        JSONObject sendObj = new JSONObject();
        sendObj.put("billDTO",objectMapper.writeValueAsString(billDTO));
        sendObj.put("guestUsername","client1@mail.com");
        sendObj.put("restaurentTableId","1");
        sendObj.put("productDTO", objectMapper.writeValueAsString(billDTO.getOrderItems().get(0).getProduct()));

        mvc = initMockMvcBillController();

        MvcResult result= mvc.perform(MockMvcRequestBuilders.post(   "/order/makeOrder").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();

        ObjectMapper mapper=new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());


        BillDTO responseBill=mapper.readValue(result.getResponse().getContentAsString(),BillDTO.class);

        sendObj = new JSONObject();
        sendObj.put("orderItem",mapper.writeValueAsString(responseBill.getOrderItems().get(0)));

        mvc=initMockMvc();
        mvc.perform(MockMvcRequestBuilders.post(   "/rest/kitchen/changeOrderItemStatus").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();

        sendObj = new JSONObject();
        sendObj.put("restaurentId",2);
        mvc = initMockMvc();
        result= mvc.perform(MockMvcRequestBuilders.post(   "/rest/kitchen/findAllTables").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        List<LinkedHashMap<String,Object>> reponse = mapper.readValue(result.getResponse().getContentAsString(), ArrayList.class);

        ArrayList billDTOS = (ArrayList)reponse.get(0).get("billDTOList");
        ArrayList orderItemList =(ArrayList)((LinkedHashMap) billDTOS.get(0)).get("orderItems");
        assertEquals(OrderStatus.READY.toString(),((LinkedHashMap)orderItemList.get(0)).get("orderStatus"));
    }


    private MenuDTO createMenuDTO() {
        List<ProductDTO> productDTOS = new ArrayList<>();
        MenuDTO menuDTO = new MenuDTO();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(3);
        productDTOS.add(productDTO);
        menuDTO.setId(2L);
        menuDTO.setProducts(productDTOS);
        return menuDTO;
    }
    private BillDTO initBillDTO() {
        RestaurantDTO restaurantDTO = new RestaurantDTO();
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setRestaurant(restaurantDTO);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1);
        productDTO.setMenu(menuDTO);
        productDTO.setOptions(new ArrayList<>());
        OptionDTO optionDTO = new OptionDTO();
        optionDTO.setName("cuisson");
        optionDTO.setCheckItemList(new ArrayList<>());
        CheckItemDTO checkItemDTO = new CheckItemDTO();
        checkItemDTO.setActive(true);
        optionDTO.getCheckItemList().add(checkItemDTO);
        productDTO.getOptions().add(optionDTO);
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

    private MockMvc initMockMvc(){
        return MockMvcBuilders.standaloneSetup(kitchenRestController).build();
    }
    private MockMvc initMockMvcBillController(){
        return MockMvcBuilders.standaloneSetup(billController).build();
    }
    private MockMvc initMockMvcMenuController(){
        return MockMvcBuilders.standaloneSetup(menuController).build();
    }

}