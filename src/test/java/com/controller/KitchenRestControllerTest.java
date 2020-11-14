package com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.model.dto.*;
import com.model.entity.OrderItem;
import com.model.enums.OrderStatus;
import com.model.enums.ProgressStatus;
import com.service.ClientService;
import org.h2.store.fs.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// TODO: all test should include assert arrange act as comments so its easier to understand code
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
    ProductController productController;

    @Autowired
    ClientService clientService;

    @Value("${config.styles.images.path}")
    private String fileBasePath;
    @Value("${front-end.url}")
    private String frontEndUrl;

    @Test
    public void testBillRetirerRestaurantTableBillNull() throws Exception {
        MockMvc mvc = initMockMvcBillController();
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();


        BillDTO billDTO = initBillDTO();
        ObjectMapper objectMapper = new ObjectMapper();


        JSONObject sendObj = new JSONObject();
        sendObj.put("billDTO", objectMapper.writeValueAsString(billDTO));
        sendObj.put("guestUsername", "client1@mail.com");
        sendObj.put("restaurentTableId", "2");
        sendObj.put("productDTO", objectMapper.writeValueAsString(billDTO.getOrderItems().get(0).getProduct()));

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/order/makeOrder").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());


        BillDTO responseBill = mapper.readValue(result.getResponse().getContentAsString(), BillDTO.class);

        sendObj = new JSONObject();
        sendObj.put("restaurentId", 2);
        mvc = initMockMvc();
        result = mvc.perform(MockMvcRequestBuilders.post("/rest/kitchen/findAllTables").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        List<LinkedHashMap<String, Object>> reponse = mapper.readValue(result.getResponse().getContentAsString(), ArrayList.class);

        ArrayList billDTOS = (ArrayList) reponse.get(1).get("bills");
        // bills
        // List<BillDTO> billDTOS =reponse.get("bills");
        assertEquals(1, billDTOS.size());

        clientService.makePayment(responseBill.getId());

        sendObj = new JSONObject();
        sendObj.put("restaurentId", 2);
        mvc = initMockMvc();
        result = mvc.perform(MockMvcRequestBuilders.post("/rest/kitchen/findAllTables").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        reponse = mapper.readValue(result.getResponse().getContentAsString(), ArrayList.class);

        billDTOS = (ArrayList) reponse.get(1).get("bills");
        // bills
        // List<BillDTO> billDTOS =reponse.get("bills");
        assertEquals(0, billDTOS.size());
    }

    @Test
    public void testFetchRestaurantChangeOrderItemStatus() throws Exception {
        MockMvc mvc = initMockMvcBillController();
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();


        BillDTO billDTO = initBillDTO();
        ObjectMapper objectMapper = new ObjectMapper();


        JSONObject sendObj = new JSONObject();
        sendObj.put("billDTO", objectMapper.writeValueAsString(billDTO));
        sendObj.put("guestUsername", "client1@mail.com");
        sendObj.put("restaurentTableId", "1");
        sendObj.put("productDTO", objectMapper.writeValueAsString(billDTO.getOrderItems().get(0).getProduct()));


        mvc = initMockMvcBillController();

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/order/makeOrder").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        billDTO = mapper.readValue(result.getResponse().getContentAsString(),BillDTO.class);

        BillDTO responseBill = mapper.readValue(result.getResponse().getContentAsString(), BillDTO.class);

        sendObj = new JSONObject();
        Long itemId = responseBill.getOrderItems().get(0).getId();
        sendObj.put("orderItemDTO", mapper.writeValueAsString(responseBill.getOrderItems().get(0)));

        mvc = initMockMvc();
        mvc.perform(MockMvcRequestBuilders.post("/rest/kitchen/changeOrderItemStatus").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();

        sendObj = new JSONObject();
        sendObj.put("restaurentId", 2);
        mvc = initMockMvc();
        result = mvc.perform(MockMvcRequestBuilders.post("/rest/kitchen/findAllTables").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ArrayList reponse = mapper.readValue(result.getResponse().getContentAsByteArray(), ArrayList.class);
        List<RestaurentTableDTO> restaurantDTOS = new ArrayList<>();
        for (Object map : reponse) {
            restaurantDTOS.add(mapper.convertValue(map, RestaurentTableDTO.class));
        }
        final Long billId = billDTO.getId();
        RestaurentTableDTO restaurentTableDTO = restaurantDTOS.stream().filter(restaurentTableDTO1 -> restaurentTableDTO1.getId() ==1L).findFirst().get();
        BillDTO theBillDTO =restaurentTableDTO.getBills().stream().filter(billDTO1 -> billId == billDTO1.getId()).findAny().get();
        OrderItemDTO orderItem =theBillDTO.getOrderItems().stream().filter(orderItemDTO -> orderItemDTO.getOrderStatus() == ProgressStatus.READY).findFirst().get();

        assertEquals(OrderStatus.READY.toString(), orderItem.getOrderStatus().toString());
    }

    @Test
    public void createRestaurentVerifRestaurantTableQRCode() throws Exception {
        MockMvc mvc = initMockMvc();
        JSONObject sendObj = new JSONObject();
        sendObj.put("ownerUsername", "owner@mail.com");
        sendObj.put("restaurantName", "le resto de momo");
        sendObj.put("nombreDeTable", "5");
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/rest/kitchen/createRestaurant").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        RestaurantDTO response = new ObjectMapper().readValue(result.getResponse().getContentAsString(), RestaurantDTO.class);
        assertNotNull(response);
        assertEquals("le resto de momo", response.getName());
        mvc = MockMvcBuilders.standaloneSetup(productController).build();
        for (RestaurentTableDTO restaurentTableDTO : response.getRestaurentTables()) {
            String pathDansProjet = fileBasePath + "qr.jpg";
            Path currentRelativePath = Paths.get("");
            String absolutePath = currentRelativePath.toAbsolutePath().toString();
            result = mvc.perform(MockMvcRequestBuilders.get("/product//getProductImg/{imgId}", restaurentTableDTO.getImgFileDTO().getId()).
                    content(sendObj.toString()).
                    contentType(MediaType.APPLICATION_JSON).
                    accept(MediaType.APPLICATION_JSON)).
                    andExpect(status().isOk()).
                    andReturn();
            byte[] bytes = result.getResponse().getContentAsByteArray();
            Path path = Paths.get(absolutePath + pathDansProjet);
            Files.write(path, bytes);
            File imgFile = new File(absolutePath + pathDansProjet);
            BufferedImage bufferedImage = ImageIO.read(imgFile);
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            try {
                Result value = new MultiFormatReader().decode(bitmap);
                assertEquals(frontEndUrl+"start?restaurantTableId=" + String.valueOf(restaurentTableDTO.getTableNumber()), value.getText());
            } catch (NotFoundException e) {
                System.out.println("There is no QR code in the image");
            }
        }
    }

    @Test
    public void testModifierNom() throws Exception {
        JSONObject sendObj = new JSONObject();
        sendObj.put("restaurantName", "le resto de momo");
        sendObj.put("restaurantId", "2");
        MockMvc mvc = initMockMvc();
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/rest/kitchen/modifierNomTable").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        RestaurantDTO restaurantDTO = mapper.readValue(result.getResponse().getContentAsString(), RestaurantDTO.class);
        assertEquals("le resto de momo", restaurantDTO.getName());

    }

    @Test
    public void testAddTable() throws Exception {
        MockMvc mvc = initMockMvc();
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/rest/kitchen/addTable/"+ 2L + "/" + 5).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());


        RestaurantDTO response = mapper.readValue(result.getResponse().getContentAsString(), RestaurantDTO.class);
        assertEquals(6, response.getRestaurentTables().size());
    }

    @Test
    public void testWaiterRequestFetchOrderItems() throws Exception {
        MockMvc mvc;
        mvc = initMockMvc();

        JSONObject sendObj = new JSONObject();
        sendObj.put("restaurentId", 2);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/rest/kitchen/getWaiterRequest").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();


        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        List<OrderItemDTO> reponse = mapper.readValue(result.getResponse().getContentAsString(), ArrayList.class);

        assertEquals(4, reponse.size());
    }

    @Test
    public void testAjouteTempsAjoute5Min() throws Exception {
        MockMvc mvc;
        mvc = initMockMvc();

        JSONObject sendObj = new JSONObject();
        sendObj.put("orderItemId", 1);
        sendObj.put("tempsAjoute", 5);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/rest/kitchen/changeOrderItemTime").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());


        OrderItemDTO response = mapper.readValue(result.getResponse().getContentAsString(), OrderItemDTO.class);
        Date date = new Date(System.currentTimeMillis());
        assertTrue(date.before(response.getTempsDePreparation()));
    }


    @Test
    public void testFetchRestaurantTableBillFound() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();

        BillDTO billDTO = initBillDTO();
        ObjectMapper objectMapper = new ObjectMapper();


        JSONObject sendObj = new JSONObject();
        sendObj.put("bill", objectMapper.writeValueAsString(billDTO));
        sendObj.put("guestUsername", "client1@mail.com");
        sendObj.put("restaurentId", "3");

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/rest/kitchen/findAllTables").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        List<RestaurentTableDTO> reponse = mapper.readValue(result.getResponse().getContentAsString(), List.class);
        assertEquals(1, reponse.size());
    }

    @Test
    public void testfindMenuParRestaurantTable() throws Exception {
        MockMvc mvc = initMockMvc();
        JSONObject sendObj = new JSONObject();
        sendObj.put("restaurantTableId","1");
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/rest/kitchen/findMenuByRestaurantId").
                content(sendObj.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        MenuDTO menuDTO = mapper.readValue(result.getResponse().getContentAsString(),MenuDTO.class);
        assertEquals(1,menuDTO.getId());
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

    private MockMvc initMockMvc() {
        return MockMvcBuilders.standaloneSetup(kitchenRestController).build();
    }

    private MockMvc initMockMvcBillController() {
        return MockMvcBuilders.standaloneSetup(billController).build();
    }

    private MockMvc initMockMvcMenuController() {
        return MockMvcBuilders.standaloneSetup(menuController).build();
    }

}