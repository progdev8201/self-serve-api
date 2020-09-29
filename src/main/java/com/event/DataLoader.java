package com.event;

import com.model.dto.OrderItemDTO;
import com.model.dto.SignUpForm;
import com.model.entity.*;
import com.model.enums.*;
import com.repository.*;
import com.service.AuthentificationService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Order(3)
@Component
@Transactional
public class DataLoader implements CommandLineRunner {

    @Value("${config.styles.images.path}")
    private String fileBasePath;

    /*@Value("${server.port}")
    private String serverPort;
*/

    @Autowired
    GuestRepository guestRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    AuthentificationService authentificationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoader.class);

    @Override
    public void run(String... args) throws Exception {
        createAccount();
    }

    private void createAccount() throws IOException {
        //create restaurant
        Restaurant restaurant = new Restaurant();
        restaurant.setName("le resto chico");
        restaurantRepository.save(restaurant);
        Bill bill = new Bill();
        bill.setOrderItems(new ArrayList<>());
        OrderItem orderItem ;


        //todo trop long pour rien mieux de faire un loop
        //create product list
        List<Product> productList = new ArrayList<>();

        Product product =createProduct(ProductMenuType.DEJEUNER,null,"download.jpg");
        orderItem = createOrderItem(ProgressStatus.READY,ProductType.SPECIAL,product);
        orderItem.setBill(bill);
        product.getOrderItems().add(orderItem);
        productList.add( product);
        bill.getOrderItems().add(orderItem);

        product =createProduct(ProductMenuType.DINER,null,"download.jpg");
        orderItem = createOrderItem(ProgressStatus.READY,ProductType.SPECIAL,product);
        orderItem.setBill(bill);
        product.getOrderItems().add(orderItem);
        productList.add( product);
        bill.getOrderItems().add(orderItem);


        product =createProduct(ProductMenuType.SOUPER,null,"download.jpg");
        orderItem =createOrderItem(ProgressStatus.PROGRESS,ProductType.WAITERREQUEST,product);
        orderItem.setBill(bill);
        product.getOrderItems().add(orderItem);
        productList.add( product);
        bill.getOrderItems().add(orderItem);

        product =createProduct(ProductMenuType.SOUPER,ProductType.SPECIAL,"download.jpg");
        orderItem =createOrderItem(ProgressStatus.PROGRESS,ProductType.WAITERREQUEST,product);
        orderItem.setBill(bill);
        product.getOrderItems().add(orderItem);
        productList.add( product);
        bill.getOrderItems().add(orderItem);

        product =createProduct(ProductMenuType.SOUPER,ProductType.SPECIAL,"download.jpg");
        productList.add( product);

        product =createProduct(ProductMenuType.SOUPER,ProductType.SPECIAL,"download.jpg");
        productList.add( product);

        product =createProduct(ProductMenuType.DINER,ProductType.CHEFCHOICE,"download.jpg");
        productList.add( product);

        product =createProduct(ProductMenuType.DINER,ProductType.CHEFCHOICE,"download.jpg");
        productList.add( product);

        product =createProduct(ProductMenuType.DINER,ProductType.CHEFCHOICE,"download.jpg");
        productList.add( product);
        product =createProduct(null,ProductType.WAITERREQUEST,"fork.png");
        productList.add( product);
        product =createProduct(null,ProductType.WAITERREQUEST,"knife.png");
        productList.add( product);
        product =createProduct(null,ProductType.WAITERREQUEST,"salt.png");
        productList.add( product);
        product =createProduct(null,ProductType.WAITERREQUEST,"sauce.png");
        productList.add( product);
        product =createProduct(null,ProductType.WAITERREQUEST,"sugar.png");
        productList.add( product);
        //create menu
        Menu menu = new Menu();
        menu.setProducts(productList);
        for(Product x:productList){
            x.setMenu(menu);

        }

        RestaurentTable restaurentTable = new RestaurentTable();
        restaurentTable.setTableNumber(1);

        restaurant = new Restaurant();
        restaurant.setBill(new ArrayList<>());
        restaurant.setName("le resto chico");
        menu.setRestaurant(restaurant);
        restaurant.setMenu(menu);
        restaurant.getBill().add(bill);
        restaurant.setRestaurentTables(new ArrayList<>());
        restaurant.getRestaurentTables().add(restaurentTable);
        restaurant = restaurantRepository.save(restaurant);

        LOGGER.info("Restaurant menuid: "+restaurant.getMenu().getId());

        if (roleRepository.findAll().size() == 0) {
            LOGGER.info("READY!...Populating database...");

            LOGGER.info("Populating RoleRepository");

            // This set is used as argument to roleRepository because converting a HashMap
            // to a Set is not straight forward
            Set<Role> roleSet = new HashSet<>();

            Arrays.stream(RoleName.values()).forEach((RoleName roleName) -> {
                LOGGER.info("Adding role: " + roleName + " to RoleRepository");
                roleSet.add(new Role(roleName));
            });

            roleRepository.saveAll(roleSet);
            roleRepository.flush();
            LOGGER.info("RoleRepository populated");


            // create client and guest
            LOGGER.info("Creating default client and guest");
            SignUpForm client = new SignUpForm("client1@mail.com", "123456", "5147887884", "client");
            SignUpForm guest = new SignUpForm("guest@mail.com", "123456", "5147887884", "guest");
            SignUpForm owner = new SignUpForm("owner@mail.com", "123456", "5147887884", "owner");

            authentificationService.registerUser(client);
            authentificationService.registerUser(guest);
            authentificationService.registerUser(owner);


            System.out.println("APPLICATION IS READY!!!");

        }
        Owner ownerEntity = ownerRepository.findByUsername("owner@mail.com").get();
        ownerEntity.getRestaurantList().add(restaurant);
        ownerRepository.save(ownerEntity);
    }

    private Product createProduct(ProductMenuType productMenuType,ProductType productType,String fileToCopy) throws IOException {
        Product product = new Product();
        product.setName("le steak chico");
        product.setPrix(29.99);
        product.setTempsDePreparation(30);

        String pathDansProjet=fileBasePath + fileToCopy;
        Path currentRelativePath = Paths.get("");
        String absolutePath = currentRelativePath.toAbsolutePath().toString();
        File imgFile = new File(absolutePath+pathDansProjet);
        ImgFile img= new ImgFile();
        img.setData(FileUtils.readFileToByteArray(imgFile));
        img.setFileType("image");
        img.setProduct(product);
        product.setImgFile(img);
        product.setProductMenuType(productMenuType);
        product.setProductType(productType);
        // product.setImgUrl(serverPort+absolutePath+1);
        product.setDescription("cest bon cest bon cest bon");
        product.setOrderItems(new ArrayList<>());
        Option option = new Option();
        option.setName("Cuisson");
        option.setCheckItemList(new ArrayList<>());
        CheckItem checkItem = new CheckItem();
        checkItem.setName("moyen");
        option.getCheckItemList().add(checkItem);
        CheckItem checkItem2 = new CheckItem();
        checkItem2.setName("faible");
        option.getCheckItemList().add(checkItem2);
        CheckItem checkItem3 = new CheckItem();
        checkItem3.setName("fort");
        option.getCheckItemList().add(checkItem3);
        product.setOptions(new ArrayList<>());
        product.getOptions().add(option);
        return product;
    }
    public OrderItem createOrderItem(ProgressStatus progressStatus,ProductType productType,Product product){
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderStatus(progressStatus);
        orderItem.setProductType(productType);
        orderItem.setProduct(product);
        orderItem.setTempsDePreparation(new Date(System.currentTimeMillis()));
        return orderItem;
    }
}
