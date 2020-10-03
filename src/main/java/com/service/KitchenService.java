package com.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.mapping.RestaurantToRestaurantDTO;
import com.model.dto.OrderItemDTO;
import com.mapping.OrderItemToOrderItemDTO;
import com.model.dto.RestaurantDTO;
import com.model.entity.*;
import com.model.enums.ProductType;
import com.model.enums.ProgressStatus;
import com.repository.OrderItemRepository;
import com.repository.OwnerRepository;
import com.repository.RestaurantRepository;
import com.service.DtoUtil.DTOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class KitchenService {
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;
    @Value("${font-end.url}")
    String frontEndUrl;

    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private DTOUtils dtoUtils;

    private final String QR_CODE_FILE_TYPE="QR Code";

    public OrderItemDTO changeOrderItemStatus(OrderItemDTO orderItemDTO) {
        OrderItem orderItem = orderItemRepository.findById(orderItemDTO.getId()).get();
        orderItem.setOrderStatus(ProgressStatus.READY);
        orderItem = orderItemRepository.save(orderItem);

        OrderItemDTO returnValue = OrderItemToOrderItemDTO.instance.convert(orderItem);
        returnValue.setProduct(dtoUtils.generateProductDTO(orderItem.getProduct()));
        return returnValue;
    }

    ///generate qrCode
    public RestaurantDTO createRestaurant(String ownerUsername, String restaurantName,int nombreDeTable) throws IOException, WriterException {
        Owner owner = ownerRepository.findByUsername(ownerUsername).get();
        Restaurant restaurant = new Restaurant();
        restaurant.setName(restaurantName);
        restaurant.setRestaurentTables(new ArrayList<>());
        for (int i=0;i<nombreDeTable;i++)
        {
            RestaurentTable restaurentTable = new RestaurentTable();
            restaurentTable.setTableNumber(i);
            ImgFile imgFile = new ImgFile();
            imgFile.setFileType("qrCode");
            imgFile.setData(generateQRCode(frontEndUrl,Integer.toString(restaurentTable.getTableNumber())));
            restaurentTable.setImgFile(imgFile);
            restaurant.getRestaurentTables().add(restaurentTable);
        }
        //add menu to restaurant
    /*    Menu menu = new Menu();
        menu.setRestaurant(restaurant);
        restaurant.setMenu(menu);*/

        restaurant = restaurantRepository.save(restaurant);
        if (Objects.isNull(owner.getRestaurantList())) {
            owner.setRestaurantList(new ArrayList<>());
        }
        restaurant.setOwner(owner);


        owner.getRestaurantList().add(restaurant);
        owner = ownerRepository.save(owner);
        Restaurant savedRestaurant =owner.getRestaurantList().stream().filter(resto -> {
            if (resto.getName().contentEquals(restaurantName))
                return true;
            return false;
        }).findFirst().get();
        RestaurantDTO restaurantDTO = dtoUtils.generateRestaurantDTO(savedRestaurant);
        return restaurantDTO;
    }

    private byte[] generateQRCode ( String frontEndUrl,String tableNumber) throws WriterException, IOException {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix =
                barcodeWriter.encode(frontEndUrl+tableNumber, BarcodeFormat.QR_CODE, 200, 200);
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage,"jpg",byteArrayOutputStream);
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
            returnValue.add(dtoUtils.generateOrderItemDTO(orderItem));
        });

        return returnValue;
    }

    public OrderItemDTO changeOrderItem(Long orderItemId, int tempsAjoute) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId).get();
        orderItem.setTempsDePreparation(new Date(orderItem.getTempsDePreparation().getTime() + (tempsAjoute * 60000)));
        return dtoUtils.generateOrderItemDTO(orderItemRepository.save(orderItem));

    }

}
