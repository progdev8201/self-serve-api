package com.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.model.dto.OrderItemDTO;
import com.model.dto.RestaurantDTO;
import com.model.dto.RestaurantEmployerDTO;
import com.model.entity.*;
import com.model.enums.MenuType;
import com.model.enums.ProgressStatus;
import com.model.enums.RestaurantType;
import com.model.enums.RoleName;
import com.repository.*;
import com.service.Util.DTOUtils;
import com.service.Util.ImgFileUtils;
import com.service.validator.RestaurantOwnerShipValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class KitchenService {
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private RestaurentTableRepository restaurentTableRepository;

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Value("${front-end.url}")
    private String frontEndUrl;

    @Autowired
    private ImgFileRepository imgFileRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ClientService clientService;

    @Autowired
    private RestaurantOwnerShipValidator restaurantOwnerShipValidator;

    @Autowired
    private DTOUtils dtoUtils;

    @Autowired
    MenuCreationService menuCreationService;

    private final String restaurantTableIdPrefix = "/start?restaurantTableId=";

    private final String QR_CODE_FILE_TYPE = "QR Code";
    private final String RESTAURANT_LOGO_FILE_TYPE = "LOGO";

    //PUBLIC METHODS


    public OrderItemDTO updateOrderItem(OrderItemDTO orderItemDTO) {
        OrderItem orderItem = orderItemRepository.findById(orderItemDTO.getId()).get();
        // on dois faire un mapping a la main , sinon , on va avoir detached entity exception
        orderItem.setOrderStatus(orderItemDTO.getOrderStatus());
        orderItem.setTempsDePreparation(orderItemDTO.getTempsDePreparation());
        orderItem.setSelected(orderItemDTO.isSelected());
        clientService.makePayment(orderItem.getBill().getId());
        return dtoUtils.mapOrderItemToOrderItemDTO(orderItemRepository.save(orderItem));
    }


    public RestaurantDTO createRestaurant(String ownerUsername, String restaurantName, int nombreDeTable, RestaurantType restaurantType) throws IOException, WriterException {
        Restaurant restaurant = initRestaurant(restaurantName, nombreDeTable, restaurantType);

        Owner owner = linkOwnerAndRestaurant(ownerUsername, restaurant);

        Restaurant savedRestaurant = findRestaurantInOwnerList(restaurantName, owner);

        return dtoUtils.mapRestaurantToRestaurantDTO(savedRestaurant);
    }

    public ResponseEntity<String> addUserToRestaurant(RestaurantEmployerDTO restaurantEmployerDTO) {
        ResponseEntity<String> FORBIDDEN = canAddRestaurant(restaurantEmployerDTO);
        if (FORBIDDEN != null) return FORBIDDEN;

        if (restaurantEmployerDTO.getRole().equals(RoleName.ROLE_COOK.toString())) {
            addCookToRestaurant(restaurantEmployerDTO);
            return ResponseEntity.ok().build();
        } else if (restaurantEmployerDTO.getRole().equals(RoleName.ROLE_WAITER.toString())) {
            addWaiterToRestaurant(restaurantEmployerDTO);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().body("The role " + restaurantEmployerDTO.getRole() + " does not exist!");
    }

    public ResponseEntity<String> updateRestaurantEmployee(RestaurantEmployerDTO restaurantEmployer) {
        Employer employer = employerRepository.findById(restaurantEmployer.getId()).get();

        if (isUsernameTaken(restaurantEmployer, employer))
            return ResponseEntity.badRequest().body("Username already exist");

        if (!restaurantEmployer.getRole().equals(RoleName.ROLE_COOK.toString()) && !restaurantEmployer.getRole().equals(RoleName.ROLE_WAITER.toString())) {
            return ResponseEntity.badRequest().body("The role " + restaurantEmployer.getRole() + " does not exist!");
        }

        updateRestaurantEmployer(restaurantEmployer);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<List<RestaurantEmployerDTO>> findAllRestaurantEmployers(Long restaurantId) {
        if (!restaurantOwnerShipValidator.hasOwnerRight(restaurantId) && !restaurantOwnerShipValidator.isAdminConnected())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        List<RestaurantEmployerDTO> restaurantEmployerDTOS = employerRepository.findAllByRestaurant_Id(restaurantId)
                .stream()
                .map(RestaurantEmployerDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(restaurantEmployerDTOS);
    }

    public RestaurantEmployerDTO findRestaurantEmployer(String username) {
        return new RestaurantEmployerDTO(employerRepository.findEmployerByUsername(username).get());
    }

    public RestaurantDTO findRestaurant(Long id) {
        return dtoUtils.mapRestaurantToRestaurantDTO(restaurantRepository.findById(id).get());
    }

    public ResponseEntity<RestaurantDTO> uploadLogo(MultipartFile file, long restaurantId) throws IOException {
        if (!restaurantOwnerShipValidator.hasOwnerRight(restaurantId) && !restaurantOwnerShipValidator.isAdminConnected())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        Restaurant restaurant = restaurantRepository.findById(restaurantId).get();

        ImgFile img = ImgFileUtils.createImgFile(file, StringUtils.cleanPath(file.getOriginalFilename()), RESTAURANT_LOGO_FILE_TYPE);

        restaurant.setImgFile(imgFileRepository.save(img));

        return ResponseEntity.ok(dtoUtils.mapRestaurantToRestaurantDTO(restaurantRepository.save(restaurant)));
    }

    public ResponseEntity deleteRestaurantTable(Long restaurantTableId, Long restaurantId) {
        if (!restaurantOwnerShipValidator.hasOwnerRight(restaurantId) && !restaurantOwnerShipValidator.isAdminConnected())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        // find restaurant then remove table then save restaurant then delete restaurant table
        Restaurant restaurant = restaurantRepository.findById(restaurantId).get();

        Optional<RestaurentTable> restaurentTable = findTableInRestaurantById(restaurantTableId, restaurant);

        restaurentTable.ifPresent(restaurantTable -> {
            removeTableFromRestaurant(restaurantTableId, restaurant, restaurantTable);
        });

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> deleteRestaurant(Long restaurantId) {
        if (!restaurantOwnerShipValidator.hasOwnerRight(restaurantId) && !restaurantOwnerShipValidator.isAdminConnected())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        //delete parent first
        Restaurant restaurant = restaurantRepository.findById(restaurantId).get();

        Owner owner = restaurant.getOwner();

        List<Restaurant> restaurants = owner.getRestaurantList().stream().filter(resto -> resto.getId() != restaurantId).collect(Collectors.toList());

        owner.setRestaurantList(restaurants);

        ownerRepository.save(owner);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<RestaurantDTO> addRestaurantTable(Long restaurantId, int tableNumber) throws IOException, WriterException {
        if (!restaurantOwnerShipValidator.hasOwnerRight(restaurantId) && !restaurantOwnerShipValidator.isAdminConnected())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        Restaurant restaurant = restaurantRepository.findById(restaurantId).get();
        if (!isTableNumberAlreadyInRestaurant(tableNumber, restaurant)) {
            restaurant.getRestaurentTables().add(createTable(tableNumber, restaurant));
            restaurant = restaurantRepository.save(restaurant);
        }

        return ResponseEntity.ok(dtoUtils.mapRestaurantToRestaurantDTO(restaurant));
    }

    public ResponseEntity<RestaurantDTO> modifierRestaurantName(String restaurantName, Long restaurantId) {
        if (!restaurantOwnerShipValidator.hasOwnerRight(restaurantId) && !restaurantOwnerShipValidator.isAdminConnected())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        Restaurant restaurant = restaurantRepository.findById(restaurantId).get();
        restaurant.setName(restaurantName);
        restaurant = restaurantRepository.save(restaurant);
        return ResponseEntity.ok(dtoUtils.mapRestaurantToRestaurantDTO(restaurant));
    }

    public OrderItemDTO changeOrderItem(Long orderItemId, int tempsAjoute) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId).get();
        orderItem.setTempsDePreparation(new Date(orderItem.getTempsDePreparation().getTime() + (tempsAjoute * 60000)));
        return dtoUtils.mapOrderItemToOrderItemDTO(orderItemRepository.save(orderItem));

    }

    public RestaurantDTO findRestaurantByRestaurantTableId(Long restaurantId) {
        RestaurentTable restaurentTable = restaurentTableRepository.findById(restaurantId).get();
        return dtoUtils.mapRestaurantToRestaurantDTO(restaurentTable.getRestaurant());
    }

    public List<OrderItemDTO> fetchWaiterRequest(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).get();

        return restaurant.getBill().stream()
                .flatMap(bill -> bill.getOrderItems().stream())
                .map(dtoUtils::mapOrderItemToOrderItemDTO)
                .collect(Collectors.toList());
    }

    public Long findEmployerRestaurantId(String username) {
        return employerRepository.findEmployerByUsername(username).get().getRestaurant().getId();
    }


    //PRIVATE METHODS
    private ResponseEntity<String> canAddRestaurant(RestaurantEmployerDTO restaurantEmployerDTO) {
        if (!restaurantOwnerShipValidator.hasOwnerRight(restaurantEmployerDTO.getRestaurantId()) && !restaurantOwnerShipValidator.isAdminConnected())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        if (employerRepository.existsByUsername(restaurantEmployerDTO.getUsername()))
            return ResponseEntity.badRequest().body("Username already exist");

        if (employerRepository.findAllByRestaurant_Id(restaurantEmployerDTO.getRestaurantId()).size() == 2)
            return ResponseEntity.badRequest().body("There is already two employer assigned");

        return null;
    }

    private boolean isUsernameTaken(RestaurantEmployerDTO restaurantEmployer, Employer employer) {
        return !employer.getUsername().equals(restaurantEmployer.getUsername()) && employerRepository.existsByUsername(restaurantEmployer.getUsername());
    }

    private void updateRestaurantEmployer(RestaurantEmployerDTO restaurantEmployerDTO) {
        employerRepository.findById(restaurantEmployerDTO.getId()).ifPresent(employer -> {
            employer.setUsername(restaurantEmployerDTO.getUsername());
            employer.setPassword(encoder.encode(restaurantEmployerDTO.getPassword()));
            employerRepository.save(employer);
        });
    }

    private void addCookToRestaurant(RestaurantEmployerDTO restaurantEmployerDTO) {
        restaurantRepository.findById(restaurantEmployerDTO.getRestaurantId()).ifPresent(restaurant -> {
            Cook cook = new Cook(restaurantEmployerDTO);
            cook.setPassword(encoder.encode(restaurantEmployerDTO.getPassword()));
            cook.setRestaurant(restaurant);
            employerRepository.save(cook);
        });
    }

    private void addWaiterToRestaurant(RestaurantEmployerDTO restaurantEmployerDTO) {
        restaurantRepository.findById(restaurantEmployerDTO.getRestaurantId()).ifPresent(restaurant -> {
            Waiter waiter = new Waiter(restaurantEmployerDTO);
            waiter.setPassword(encoder.encode(restaurantEmployerDTO.getPassword()));
            waiter.setRestaurant(restaurant);
            employerRepository.save(waiter);
        });
    }

    private boolean isOrderItemToFetch(OrderItem orderItem) {
        return (orderItem.getOrderStatus() == ProgressStatus.READY) ||
                (orderItem.getMenuType() == MenuType.WAITERREQUEST) ||
                (orderItem.getMenuType() == MenuType.WAITERCALL);
    }

    private Restaurant initRestaurant(String restaurantName, int nombreDeTable, RestaurantType restaurantType) throws WriterException, IOException {
        Restaurant restaurant = initRestaurant(restaurantName, restaurantType);
        initMenus(restaurant);
        createTables(nombreDeTable, restaurant);

        return restaurant;
    }

    private void initMenus(Restaurant restaurant) throws IOException {
        restaurant.getMenus().add(menuCreationService.createMenuRequest());
    }

    private RestaurentTable createTable(int tableNumber, Restaurant restaurant) throws WriterException, IOException {
        RestaurentTable restaurentTable = new RestaurentTable();
        restaurentTable.setTableNumber(tableNumber);
        restaurentTable.setRestaurant(restaurant);
        ImgFile imgFile = ImgFileUtils.createImgFile(generateQRCode(frontEndUrl, Integer.toString(restaurentTable.getTableNumber())), QR_CODE_FILE_TYPE);
        restaurentTable.setImgFile(imgFile);
        return restaurentTable;
    }

    private byte[] generateQRCode(String frontEndUrl, String tableNumber) throws WriterException, IOException {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(frontEndUrl + restaurantTableIdPrefix + tableNumber, BarcodeFormat.QR_CODE, 200, 200);
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
        byteArrayOutputStream.flush();

        return byteArrayOutputStream.toByteArray();
    }

    private Restaurant findRestaurantInOwnerList(String restaurantName, Owner owner) {
        Restaurant savedRestaurant = owner.getRestaurantList().stream().filter(resto -> resto.getName().contentEquals(restaurantName)).findFirst().get();
        return savedRestaurant;
    }

    private Owner linkOwnerAndRestaurant(String ownerUsername, Restaurant restaurant) {
        Owner owner = ownerRepository.findByUsername(ownerUsername).get();
        // restaurant = restaurantRepository.save(restaurant);
        if (Objects.isNull(owner.getRestaurantList())) {
            owner.setRestaurantList(new ArrayList<>());
        }
        restaurant.setOwner(owner);

        owner.getRestaurantList().add(restaurant);
        owner = ownerRepository.save(owner);
        return owner;
    }


    private void createTables(int nombreDeTable, Restaurant restaurant) throws WriterException, IOException {
        for (int i = 0; i < nombreDeTable; i++)
            restaurant.getRestaurentTables().add(createTable(i, restaurant));
    }

    private Restaurant initRestaurant(String restaurantName, RestaurantType restaurantType) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(restaurantName);
        restaurant.setRestaurentTables(new ArrayList<>());
        restaurant.setMenus(new ArrayList<>());
        restaurant.setRestaurantType(restaurantType);
        return restaurant;
    }

    private void removeTableFromRestaurant(Long restaurantTableId, Restaurant restaurant, RestaurentTable restaurantTable) {
        restaurant.getRestaurentTables().remove(restaurantTable);

        restaurantRepository.saveAndFlush(restaurant);

        restaurentTableRepository.deleteById(restaurantTableId);
    }

    private Optional<RestaurentTable> findTableInRestaurantById(Long restaurantTableId, Restaurant restaurant) {
        return restaurant.getRestaurentTables().stream().filter(table -> table.getId().equals(restaurantTableId)).findFirst();
    }

    private boolean isTableNumberAlreadyInRestaurant(int tableNumber, Restaurant restaurant) {
        return restaurant.getRestaurentTables().stream().filter(table -> table.getTableNumber() == tableNumber).findFirst().isPresent();
    }
}
