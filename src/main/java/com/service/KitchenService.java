package com.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.mapping.OrderItemToOrderItemDTO;
import com.model.dto.OrderItemDTO;
import com.model.dto.RestaurantDTO;
import com.model.dto.RestaurantUserDto;
import com.model.entity.*;
import com.model.enums.MenuType;
import com.model.enums.ProgressStatus;
import com.model.enums.RoleName;
import com.repository.*;
import com.service.Util.DTOUtils;
import com.service.Util.ImgFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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
    private CookRepository cookRepository;

    @Autowired
    private WaiterRepository waiterRepository;

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
    private DTOUtils dtoUtils;

    private final String restaurantTableIdPrefix = "start?restaurantTableId=";

    private final String QR_CODE_FILE_TYPE = "QR Code";
    private final String RESTAURANT_LOGO_FILE_TYPE = "LOGO";

    //PUBLIC METHODS

    public OrderItemDTO changeOrderItemStatus(OrderItemDTO orderItemDTO) {
        OrderItem orderItem = orderItemRepository.findById(orderItemDTO.getId()).get();

        orderItem.setOrderStatus(ProgressStatus.READY);

        return constructReturnValue(orderItemRepository.save(orderItem));
    }


    public RestaurantDTO createRestaurant(String ownerUsername, String restaurantName, int nombreDeTable) throws IOException, WriterException {
        Restaurant restaurant = initRestaurant(restaurantName, nombreDeTable);

        Owner owner = linkOwnerAndRestaurant(ownerUsername, restaurant);

        Restaurant savedRestaurant = findRestaurantInOwnerList(restaurantName, owner);

        return dtoUtils.mapRestaurantToRestaurantDTO(savedRestaurant);
    }

    public ResponseEntity<String> addUserToRestaurant(RestaurantUserDto restaurantUserDto) {
        if (restaurantUserDto.getRole().equals(RoleName.ROLE_COOK)) {
            addCookToRestaurant(restaurantUserDto);
            return ResponseEntity.ok().build();
        } else if (restaurantUserDto.getRole().equals(RoleName.ROLE_WAITER)) {
            addWaiterToRestaurant(restaurantUserDto);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().body("The role attributed does not exist!");
    }

    public ResponseEntity<String> updateRestaurantEmployee(RestaurantUserDto restaurantUserDto) {
        if (restaurantUserDto.getRole().equals(RoleName.ROLE_COOK)) {
            updateRestaurantCook(restaurantUserDto);
            return ResponseEntity.ok().build();
        } else if (restaurantUserDto.getRole().equals(RoleName.ROLE_WAITER)) {
            updateRestaurantWaiter(restaurantUserDto);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().body("The role attributed does not exist");
    }

    public List<RestaurantUserDto> findRestaurantEmployees(Long restaurantId){
        List<RestaurantUserDto> restaurantUserDtos = new ArrayList<>();

        cookRepository.findCookByRestaurant_Id(restaurantId).ifPresent(cook -> restaurantUserDtos.add(new RestaurantUserDto(cook)));

        waiterRepository.findWaiterByRestaurant_Id(restaurantId).ifPresent(waiter -> restaurantUserDtos.add(new RestaurantUserDto(waiter)));

        return restaurantUserDtos;
    }

    public RestaurantDTO uploadLogo(MultipartFile file, long restaurantId) throws IOException {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).get();

        ImgFile img = ImgFileUtils.createImgFile(file, StringUtils.cleanPath(file.getOriginalFilename()), RESTAURANT_LOGO_FILE_TYPE);

        restaurant.setImgFile(imgFileRepository.save(img));

        return dtoUtils.mapRestaurantToRestaurantDTO(restaurantRepository.save(restaurant));
    }

    public void deleteRestaurantTable(Long restaurantTableId, Long restaurantId) {
        // find restaurant then remove table then save restaurant then delete restaurant table
        Restaurant restaurant = restaurantRepository.findById(restaurantId).get();

        Optional<RestaurentTable> restaurentTable = findTableInRestaurantById(restaurantTableId, restaurant);

        restaurentTable.ifPresent(restaurantTable -> {
            removeTableFromRestaurant(restaurantTableId, restaurant, restaurantTable);
        });
    }

    public void deleteRestaurant(Long restaurantId) {
        //delete parent first
        Restaurant restaurant = restaurantRepository.findById(restaurantId).get();

        Owner owner = restaurant.getOwner();

        List<Restaurant> restaurants = owner.getRestaurantList().stream().filter(resto -> resto.getId() != restaurantId).collect(Collectors.toList());

        owner.setRestaurantList(restaurants);

        ownerRepository.save(owner);
    }

    public RestaurantDTO addRestaurantTable(Long restaurantId, int tableNumber) throws IOException, WriterException {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).get();
        if (!isTableNumberAlreadyInRestaurant(tableNumber, restaurant)) {
            restaurant.getRestaurentTables().add(createTable(tableNumber, restaurant));
            restaurant = restaurantRepository.save(restaurant);
        }

        return dtoUtils.mapRestaurantToRestaurantDTO(restaurant);
    }

    public RestaurantDTO modifierRestaurantName(String restaurantName, Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).get();
        restaurant.setName(restaurantName);
        restaurant = restaurantRepository.save(restaurant);
        return dtoUtils.mapRestaurantToRestaurantDTO(restaurant);
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
        List<OrderItem> orderItemList = new ArrayList<>();
        List<OrderItemDTO> returnValue = new ArrayList<>();

        restaurant.getBill().forEach(bill -> {
            orderItemList.addAll(bill.getOrderItems().stream().filter(orderItem ->
                    isOrderItemToFetch(orderItem))
                    .collect(Collectors.toList()));
        });

        orderItemList.forEach(orderItem -> {
            returnValue.add(dtoUtils.mapOrderItemToOrderItemDTO(orderItem));
        });

        return returnValue;
    }

    public Long findCookRestaurantId(String username){
        return cookRepository.findCookByUsername(username).get().getRestaurant().getId();
    }

    public Long findWaiterRestaurantId(String username){
        System.out.println("My id");
        System.out.println(waiterRepository.findWaiterByUsername(username).get().getRestaurant().getId());
        return waiterRepository.findWaiterByUsername(username).get().getRestaurant().getId();
    }

    //PRIVATE METHODS
    private void updateRestaurantWaiter(RestaurantUserDto restaurantUserDto) {
        waiterRepository.findById(restaurantUserDto.getId()).ifPresent(waiter -> {
            waiter.setUsername(restaurantUserDto.getUsername());
            waiter.setPassword(encoder.encode(restaurantUserDto.getPassword()));
            waiterRepository.save(waiter);
        });
    }

    private void updateRestaurantCook(RestaurantUserDto restaurantUserDto) {
        cookRepository.findById(restaurantUserDto.getId()).ifPresent(cook -> {
            cook.setUsername(restaurantUserDto.getUsername());
            cook.setPassword(encoder.encode(restaurantUserDto.getPassword()));
            cookRepository.save(cook);
        });
    }

    private void addCookToRestaurant(RestaurantUserDto restaurantUserDto) {
        restaurantRepository.findById(restaurantUserDto.getRestaurantId()).ifPresent(restaurant -> {
            Cook cook = new Cook(restaurantUserDto);
            cook.setPassword(encoder.encode(cook.getPassword()));
            cook.setRestaurant(restaurant);
            cookRepository.save(cook);
        });
    }

    private void addWaiterToRestaurant(RestaurantUserDto restaurantUserDto) {
        restaurantRepository.findById(restaurantUserDto.getRestaurantId()).ifPresent(restaurant -> {
            Waiter waiter = new Waiter(restaurantUserDto);
            waiter.setPassword(encoder.encode(waiter.getPassword()));
            waiter.setRestaurant(restaurant);
            waiterRepository.save(waiter);
        });
    }

    private boolean isOrderItemToFetch(OrderItem orderItem) {
        return (orderItem.getOrderStatus() == ProgressStatus.READY) ||
                (orderItem.getMenuType() == MenuType.WAITERREQUEST) ||
                (orderItem.getMenuType() == MenuType.WAITERCALL);
    }

    private Restaurant initRestaurant(String restaurantName, int nombreDeTable) throws WriterException, IOException {
        Restaurant restaurant = initRestaurant(restaurantName);

        createTables(nombreDeTable, restaurant);

        return restaurant;
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
        Restaurant savedRestaurant = owner.getRestaurantList().stream().filter(resto -> {
            if (resto.getName().contentEquals(restaurantName))
                return true;
            return false;
        }).findFirst().get();
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

    private Restaurant initRestaurant(String restaurantName) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(restaurantName);
        restaurant.setRestaurentTables(new ArrayList<>());
        return restaurant;
    }

    private OrderItemDTO constructReturnValue(OrderItem orderItem) {
        OrderItemDTO returnValue = OrderItemToOrderItemDTO.instance.convert(orderItem);
        returnValue.setProduct(dtoUtils.mapProductToProductDTO(orderItem.getProduct()));
        return returnValue;
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
