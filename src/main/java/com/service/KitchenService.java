package com.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.mapping.OrderItemToOrderItemDTO;
import com.model.dto.MenuDTO;
import com.model.dto.OrderItemDTO;
import com.model.dto.RestaurantDTO;
import com.model.entity.*;
import com.model.enums.ProductType;
import com.model.enums.ProgressStatus;
import com.repository.*;
import com.service.DtoUtil.DTOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private RestaurantRepository restaurantRepository;
    @Value("${front-end.url}")
    String frontEndUrl;

    @Autowired
    ImgFileRepository imgFileRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private DTOUtils dtoUtils;

    private final String restaurantTableIdPrefix = "start?restaurantTableId=";

    private final String QR_CODE_FILE_TYPE = "QR Code";

    public OrderItemDTO changeOrderItemStatus(OrderItemDTO orderItemDTO) {
        OrderItem orderItem = orderItemRepository.findById(orderItemDTO.getId()).get();
        orderItem.setOrderStatus(ProgressStatus.READY);
        orderItem = orderItemRepository.save(orderItem);

        OrderItemDTO returnValue = OrderItemToOrderItemDTO.instance.convert(orderItem);
        returnValue.setProduct(dtoUtils.mapProductToProductDTO(orderItem.getProduct()));
        return returnValue;
    }

    //todo clean code
    ///generate qrCode
    public RestaurantDTO createRestaurant(String ownerUsername, String restaurantName, int nombreDeTable) throws IOException, WriterException {
        //find owner
        Owner owner = ownerRepository.findByUsername(ownerUsername).get();

        //create
        Restaurant restaurant = new Restaurant();

        // set restaurant
        restaurant.setName(restaurantName);
        restaurant.setRestaurentTables(new ArrayList<>());

        //add tables to restaurant
        for (int i = 0; i < nombreDeTable; i++)
            restaurant.getRestaurentTables().add(createTable(i, restaurant));

        //create menu
        Menu menu = new Menu();

        //bind menu to restaurant
        menu.setRestaurant(restaurant);
        restaurant.setMenu(menu);

        //save restaurant
        restaurant = restaurantRepository.save(restaurant);

        //create new restaurant list if list is null
        if (Objects.isNull(owner.getRestaurantList())) {
            owner.setRestaurantList(new ArrayList<>());
        }

        // bind restaurant to owner
        restaurant.setOwner(owner);
        owner.getRestaurantList().add(restaurant);

        // save owner
        owner = ownerRepository.save(owner);

        //find saved restaurant
        Restaurant savedRestaurant = owner.getRestaurantList().stream().filter(resto -> {
            if (resto.getName().contentEquals(restaurantName))
                return true;
            return false;
        }).findFirst().get();

        // map restaurant
        RestaurantDTO restaurantDTO = dtoUtils.mapRestaurantToRestaurantDTO(savedRestaurant);

        //return restaurant
        return restaurantDTO;
    }
    public RestaurantDTO uploadLogo(MultipartFile file, long restaurantId) throws IOException {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).get();

        ImgFile img = new ImgFile();
        img.setFileType(file.getContentType());
        img.setFileName(StringUtils.cleanPath(file.getOriginalFilename()));
        img.setData(file.getBytes());

        restaurant.setImgFile(imgFileRepository.save(img));
        restaurant = restaurantRepository.save(restaurant);

        return dtoUtils.mapRestaurantToRestaurantDTO(restaurant);
    }
    private RestaurentTable createTable(int tableNumber, Restaurant restaurant) throws WriterException, IOException {
        RestaurentTable restaurentTable = new RestaurentTable();
        restaurentTable.setTableNumber(tableNumber);
        restaurentTable.setRestaurant(restaurant);
        ImgFile imgFile = new ImgFile();
        imgFile.setFileType("qrCode");
        imgFile.setData(generateQRCode(frontEndUrl, Integer.toString(restaurentTable.getTableNumber())));
        restaurentTable.setImgFile(imgFile);
        return restaurentTable;
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
        if (!restaurant.getRestaurentTables().stream().filter(table -> table.getTableNumber() == tableNumber).findFirst().isPresent()){
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

    public void deleteRestaurantTable(Long restaurantTableId, Long restaurantId) {
        // find restaurant then remove table then save restaurant then delete restaurant table
        Restaurant restaurant = restaurantRepository.findById(restaurantId).get();

        Optional<RestaurentTable> restaurentTable = restaurant.getRestaurentTables().stream().filter(table -> table.getId().equals(restaurantTableId)).findFirst();

        restaurentTable.ifPresent(restaurantTable -> {
            restaurant.getRestaurentTables().remove(restaurantTable);

            restaurantRepository.saveAndFlush(restaurant);

            restaurentTableRepository.deleteById(restaurantTableId);
        });
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

    public List<OrderItemDTO> fetchWaiterRequest(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).get();
        List<OrderItem> orderItemList = new ArrayList<>();
        List<OrderItemDTO> returnValue = new ArrayList<>();

        restaurant.getBill().forEach(bill -> {
            orderItemList.addAll(bill.getOrderItems().stream().filter(orderItem ->
                    (orderItem.getOrderStatus() == ProgressStatus.READY) ||
                            (orderItem.getProductType() == ProductType.WAITERREQUEST) ||
                            (orderItem.getProductType() == ProductType.WAITERCALL))
                    .collect(Collectors.toList()));
        });

        orderItemList.forEach(orderItem -> {
            returnValue.add(dtoUtils.mapOrderItemToOrderItemDTO(orderItem));
        });

        return returnValue;
    }

    public OrderItemDTO changeOrderItem(Long orderItemId, int tempsAjoute) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId).get();
        orderItem.setTempsDePreparation(new Date(orderItem.getTempsDePreparation().getTime() + (tempsAjoute * 60000)));
        return dtoUtils.mapOrderItemToOrderItemDTO(orderItemRepository.save(orderItem));

    }

    public MenuDTO menuParRestaurantTable(Long restaurantId) {
        RestaurentTable restaurentTable = restaurentTableRepository.findById(restaurantId).get();
        MenuDTO menuDTO =dtoUtils.generateMenuDTO(restaurentTable.getRestaurant().getMenu());
        menuDTO.setRestaurant(dtoUtils.mapRestaurantToRestaurantDTO(restaurentTable.getRestaurant()));
        return menuDTO;
    }

}
