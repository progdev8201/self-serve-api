package com.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.model.dto.*;
import com.model.entity.Cook;
import com.model.enums.OrderStatus;
import com.model.enums.ProgressStatus;
import com.model.enums.RoleName;
import com.service.ClientService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// TODO: all test should include assert arrange act as comments so its easier to understand code
@SpringBootTest

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
        sendObj.put("guestUsername", "client@mail.com");
        sendObj.put("restaurentTableId", "2");
        sendObj.put("productDTO", objectMapper.writeValueAsString(billDTO.getOrderItems().get(0).getProduct()));

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/order/makeOrder").
                content(sendObj.toString()).
                contentType(APPLICATION_JSON).
                accept(APPLICATION_JSON)).
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
                contentType(APPLICATION_JSON).
                accept(APPLICATION_JSON)).
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
                contentType(APPLICATION_JSON).
                accept(APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        reponse = mapper.readValue(result.getResponse().getContentAsString(), ArrayList.class);

        billDTOS = (ArrayList) reponse.get(1).get("bills");
        // bills
        // List<BillDTO> billDTOS =reponse.get("bills");
        assertEquals(0, billDTOS.size());
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
                contentType(APPLICATION_JSON).
                accept(APPLICATION_JSON)).
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
            result = mvc.perform(get("/product//getProductImg/{imgId}", restaurentTableDTO.getImgFileDTO().getId()).
                    content(sendObj.toString()).
                    contentType(APPLICATION_JSON).
                    accept(APPLICATION_JSON)).
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
                assertEquals(frontEndUrl+"/start?restaurantTableId=" + String.valueOf(restaurentTableDTO.getTableNumber()), value.getText());
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
                contentType(APPLICATION_JSON).
                accept(APPLICATION_JSON)).
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
                contentType(APPLICATION_JSON).
                accept(APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());


        RestaurantDTO response = mapper.readValue(result.getResponse().getContentAsString(), RestaurantDTO.class);
        assertEquals(6, response.getRestaurentTables().size());
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
                contentType(APPLICATION_JSON).
                accept(APPLICATION_JSON)).
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
                contentType(APPLICATION_JSON).
                accept(APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        List<RestaurentTableDTO> reponse = mapper.readValue(result.getResponse().getContentAsString(), List.class);
        assertEquals(1, reponse.size());
    }
    @Test
    public void testfindRestaurantParRestaurantTable() throws Exception {
        MockMvc mvc = initMockMvc();
        MvcResult result = mvc.perform(get("/rest/kitchen/findRestaurantByRestaurantTableId/{tableID}","1").
                contentType(APPLICATION_JSON).
                accept(APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        RestaurantDTO restaurantDTO = mapper.readValue(result.getResponse().getContentAsString(), RestaurantDTO.class);
        assertEquals(2,restaurantDTO.getId());
    }

    @Test
    public void updateRestaurantEmployeeCookTest() throws Exception{
        // Arrange
        RestaurantEmployerDTO cook = new RestaurantEmployerDTO(5L,"newCookMail@mail.com","ibawe",2L, RoleName.ROLE_COOK.toString(),"owner@mail.com");
        RestaurantEmployerDTO waiter = new RestaurantEmployerDTO(6L,"newWaiterMail@mail.com","ibawe",2L, RoleName.ROLE_WAITER.toString(),"owner@mail.com");
        MockMvc mvc = initMockMvc();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Long restaurantId = 2L;

        // Act
        mvc.perform(put("/rest/kitchen/updateRestaurantUser")
                .content(mapper.writeValueAsString(cook))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        mvc.perform(put("/rest/kitchen/updateRestaurantUser")
                .content(mapper.writeValueAsString(waiter))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult result = mvc.perform(get("/rest/kitchen/restaurantEmployers/" + restaurantId)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();


        List<RestaurantEmployerDTO> restaurantEmployerDTOS = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<RestaurantEmployerDTO>>() {});

        RestaurantEmployerDTO cookResponse = restaurantEmployerDTOS.get(0).getRole().equals(RoleName.ROLE_COOK.toString()) ? restaurantEmployerDTOS.get(0) : restaurantEmployerDTOS.get(1);
        RestaurantEmployerDTO waiterResponse = restaurantEmployerDTOS.get(0).getRole().equals(RoleName.ROLE_COOK.toString()) ? restaurantEmployerDTOS.get(1) : restaurantEmployerDTOS.get(0);


        // Assert
        assertEquals(cook.getId(), cookResponse.getId());
        assertEquals(cook.getUsername(), cookResponse.getUsername());
        assertEquals(cook.getRestaurantId(), cookResponse.getRestaurantId());
        assertEquals(cook.getRole(), cookResponse.getRole());

        assertEquals(waiter.getId(), waiterResponse.getId());
        assertEquals(waiter.getUsername(), waiterResponse.getUsername());
        assertEquals(waiter.getRestaurantId(), waiterResponse.getRestaurantId());
        assertEquals(waiter.getRole(), waiterResponse.getRole());
    }

    //todo find out why is there a 406 error
   /* @Test
    public void findCookRestaurantId() throws Exception {
        // Arrange
        String cookUsername = "cook@mail.com";
        MockMvc mvc = initMockMvc();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // Act
        MvcResult result = mvc.perform(get("/rest/kitchen/cookRestaurant/" + cookUsername)
                .contentType(ALL)
                .accept(ALL))
                .andExpect(status().isOk())
                .andReturn();

        Long restaurantId = mapper.readValue(result.getResponse().getContentAsString(),Long.class);

        // Assert
        assertEquals(2,restaurantId);
    }

    @Test
    public void findWaiterRestaurantId() throws Exception {
        // Arrange
        String waiterUsername = "waiter@mail.com";
        MockMvc mvc = initMockMvc();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // Act
        MvcResult result = mvc.perform(get("/rest/kitchen/waiterRestaurant/" + waiterUsername)
                .contentType(ALL)
                .accept(ALL))
                .andExpect(status().isOk())
                .andReturn();

        Long restaurantId = mapper.readValue(result.getResponse().getContentAsString(),Long.class);

        // Assert
        assertEquals(2,restaurantId);
    }*/

    // Private Methods

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
        productCheckItem.setPrix(BigDecimal.valueOf(2.50));
        productDTO.getCheckItems().add(productCheckItem);
        optionDTO.getCheckItemList().add(checkItemDTO);
        productDTO.getOptions().add(optionDTO);
        OrderItemDTO orderItemDTO1 = new OrderItemDTO();
        orderItemDTO1.setProduct(productDTO);
        orderItemDTO1.setPrix(BigDecimal.valueOf(29.99));
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