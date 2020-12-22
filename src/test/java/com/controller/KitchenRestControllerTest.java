package com.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.model.dto.*;
import com.model.entity.*;
import com.model.enums.OrderStatus;
import com.model.enums.ProgressStatus;
import com.model.enums.RoleName;
import com.repository.*;
import com.service.ClientService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

    @MockBean
    ProductRepository productRepository;

    @MockBean
    OwnerRepository ownerRepository;

    @MockBean
    OrderItemRepository orderItemRepository;

    @MockBean
    RestaurentTableRepository restaurentTableRepository;

    @MockBean
    EmployerRepository employerRepository;

    @MockBean
    RestaurantRepository restaurantRepository;

    @Captor
    ArgumentCaptor<Employer> employerArgumentCaptor;

    @Captor
    ArgumentCaptor<Owner> ownerArgumentCaptor;

    @Value("${config.styles.images.path}")
    private String fileBasePath;
    @Value("${front-end.url}")
    private String frontEndUrl;


    @Test
    public void createRestaurentVerifRestaurantTableQRCode() throws Exception {
        MockMvc mvc = initMockMvc();
        JSONObject sendObj = new JSONObject();
        sendObj.put("ownerUsername", "owner@mail.com");
        sendObj.put("restaurantName", "le resto de momo");
        sendObj.put("nombreDeTable", "5");

        Mockito.when(ownerRepository.findByUsername(any(String.class))).thenReturn(Optional.of(new Owner()));


        Mockito.when(ownerRepository.save(ownerArgumentCaptor.capture())).thenAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                return (Owner) invocation.getArguments()[0];
            }
        });

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
        Owner owner = ownerArgumentCaptor.getValue();
        for (RestaurentTable restaurentTable : ownerArgumentCaptor.getValue().getRestaurantList().get(0).getRestaurentTables()) {
            String pathDansProjet = fileBasePath + "qr.jpg";
            Path currentRelativePath = Paths.get("");
            String absolutePath = currentRelativePath.toAbsolutePath().toString();

            byte[] bytes = restaurentTable.getImgFile().getData();
            Path path = Paths.get(absolutePath + pathDansProjet);
            Files.write(path, bytes);
            File imgFile = new File(absolutePath + pathDansProjet);
            BufferedImage bufferedImage = ImageIO.read(imgFile);
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            try {
                Result value = new MultiFormatReader().decode(bitmap);
                assertEquals(frontEndUrl+"/start?restaurantTableId=" + String.valueOf(restaurentTable.getTableNumber()), value.getText());
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
        mockRestaurantRepo();
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
        mockRestaurantRepo();
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/rest/kitchen/addTable/"+ 2L + "/" + 5).
                contentType(APPLICATION_JSON).
                accept(APPLICATION_JSON)).
                andExpect(status().isOk()).
                andReturn();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // mock repo a un table de base
        RestaurantDTO response = mapper.readValue(result.getResponse().getContentAsString(), RestaurantDTO.class);
        assertEquals(2, response.getRestaurentTables().size());
    }


    @Test
    public void testAjouteTempsAjoute5Min() throws Exception {
        MockMvc mvc;
        mvc = initMockMvc();

        OrderItem orderItem = new OrderItem();
        orderItem.setTempsDePreparation(new Date(System.currentTimeMillis()));
        Date date = new Date(orderItem.getTempsDePreparation().getTime());

        Mockito.when(orderItemRepository.findById(any(Long.class))).thenReturn(Optional.of(orderItem));

        Mockito.when(orderItemRepository.save(any(OrderItem.class))).thenAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                return (OrderItem) invocation.getArguments()[0];
            }
        });

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
        assertTrue(date.before(response.getTempsDePreparation()));
        assertEquals(date.getTime()+(5*60000),response.getTempsDePreparation().getTime());
    }


    @Test
    public void testFetchRestaurantTableBillFound() throws Exception {
        MockMvc mvc = initMockMvc();
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();

        BillDTO billDTO = initBillDTO();
        ObjectMapper objectMapper = new ObjectMapper();

        mockRestaurantRepo();


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
        mockRestaurantRepo();

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
        MockMvc mvc = initMockMvc();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Long restaurantId = 2L;


        Restaurant restaurant = new Restaurant();
        restaurant.setId(cook.getRestaurantId());
        Cook cookEntity = new Cook();
        cookEntity.setUsername(cook.getUsername());
        cookEntity.setId(cook.getId());
        cookEntity.setRestaurant(restaurant);
        cookEntity.setRole( RoleName.ROLE_COOK.toString());

        Mockito.when(employerRepository.findById(any(Long.class))).thenReturn(Optional.of(cookEntity));



        // Act
        mvc.perform(put("/rest/kitchen/updateRestaurantUser")
                .content(mapper.writeValueAsString(cook))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();


        Mockito.verify(employerRepository).save(employerArgumentCaptor.capture());

        // Assert
        assertEquals(cook.getId(), employerArgumentCaptor.getValue().getId());
        assertEquals(cook.getUsername(), employerArgumentCaptor.getValue().getUsername());
        assertEquals(cook.getRestaurantId(), employerArgumentCaptor.getValue().getRestaurant().getId());
        assertEquals(cook.getRole(), employerArgumentCaptor.getValue().getRole());

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
        productCheckItem.setPrix(2.50);
        productDTO.getCheckItems().add(productCheckItem);
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



    private void mockRestaurantRepo() {
        Product product = new Product();
        product.setImgFile(new ImgFile());

        OrderItem orderItem = new OrderItem();
        orderItem.setCheckItems(new ArrayList<>());
        orderItem.setOption(new ArrayList<>());
        orderItem.setProduct(product);


        Bill bill = new Bill();
        bill.setOrderItems(new ArrayList<>());
        bill.getOrderItems().add(orderItem);

        Restaurant restaurant = new Restaurant();
        restaurant.setName("chica");
        restaurant.setId(2L);
        restaurant.setRestaurentTables(new ArrayList<>());

        restaurant.setImgFile(new ImgFile());
        restaurant.setBill(new ArrayList<>());
        restaurant.getBill().add(bill);

        RestaurentTable restaurentTable = new RestaurentTable();
        restaurentTable.setBills(new ArrayList<>());
        restaurentTable.getBills().add(bill);
        restaurentTable.setRestaurant(restaurant);

        restaurant.getRestaurentTables().add(restaurentTable);

        Mockito.when(restaurentTableRepository.findById(any(Long.class))).thenReturn(Optional.of(restaurentTable));
        Mockito.when(restaurantRepository.findById(any(Long.class))).thenReturn(Optional.of(restaurant));
        Mockito.when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                return (Restaurant) invocation.getArguments()[0];
            }
        });
    }
}