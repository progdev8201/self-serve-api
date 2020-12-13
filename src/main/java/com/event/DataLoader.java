package com.event;

import com.google.zxing.WriterException;
import com.model.dto.RestaurantDTO;
import com.model.dto.RestaurantEmployerDTO;
import com.model.dto.SignUpForm;
import com.model.entity.*;
import com.model.enums.*;
import com.repository.*;
import com.service.AuthentificationService;
import com.service.KitchenService;
import com.service.StripeService;
import com.stripe.exception.StripeException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Order(3)
@Profile("!prod")
@Component
@Transactional
public class DataLoader implements CommandLineRunner {

    @Value("${config.styles.images.path}")
    private String fileBasePath;

    /*@Value("${server.port}")
    private String serverPort;
*/

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private KitchenService kitchenService;


    @Autowired
    private ResourceLoader resourceLoader;


    @Autowired
    private AuthentificationService authentificationService;

    @Autowired
    private StripeService stripeService;

    @Autowired
    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoader.class);

    @Override
    public void run(String... args) throws Exception {
        createAccount();
    }

    private void createAccount() throws IOException, WriterException, StripeException {
        stripeService.initApplePay();
        //create restaurant
        Restaurant restaurant = new Restaurant();

        restaurantRepository.save(restaurant);
        Bill bill = new Bill();
        bill.setOrderItems(new ArrayList<>());
        OrderItem orderItem;
        Menu menuDiner = createMenu("Diner", MenuType.FOOD);
        Menu menuSouper = createMenu("Souper", MenuType.FOOD);
        Menu menuDejeuner = createMenu("Dejeuner", MenuType.FOOD);
        Menu menuRequest = createMenu("request", MenuType.WAITERREQUEST);
        //todo trop long pour rien mieux de faire un loop
        //create product list
        List<Product> productList = new ArrayList<>();

        Product product = createProduct( MenuType.FOOD, "download.jpg", menuDejeuner);
        orderItem = createOrderItem(ProgressStatus.READY, MenuType.FOOD, product);
        orderItem.setBill(bill);
        product.getOrderItems().add(orderItem);
        productList.add(product);
        bill.getOrderItems().add(orderItem);

        product = createProduct( MenuType.FOOD, "download.jpg", menuDejeuner);
        orderItem = createOrderItem(ProgressStatus.READY, MenuType.FOOD, product);
        orderItem.setBill(bill);
        product.getOrderItems().add(orderItem);
        productList.add(product);
        bill.getOrderItems().add(orderItem);


        product = createProduct( MenuType.FOOD, "download.jpg", menuDejeuner);
        orderItem = createOrderItem(ProgressStatus.PROGRESS, MenuType.WAITERREQUEST, product);
        orderItem.setBill(bill);
        product.getOrderItems().add(orderItem);
        productList.add(product);
        bill.getOrderItems().add(orderItem);

        product = createProduct( MenuType.FOOD, "download.jpg", menuDiner);
        orderItem = createOrderItem(ProgressStatus.PROGRESS, MenuType.WAITERREQUEST, product);
        orderItem.setBill(bill);
        product.getOrderItems().add(orderItem);
        productList.add(product);
        bill.getOrderItems().add(orderItem);

        product = createProduct( MenuType.FOOD, "download.jpg", menuDiner);
        productList.add(product);

        product = createProduct( MenuType.FOOD, "download.jpg", menuDiner);
        productList.add(product);

        product = createProduct( MenuType.FOOD, "download.jpg", menuSouper);
        productList.add(product);

        product = createProduct( MenuType.FOOD, "download.jpg", menuSouper);
        productList.add(product);

        product = createProduct( MenuType.FOOD, "download.jpg", menuSouper);
        productList.add(product);

        product = createProduct( MenuType.WAITERREQUEST, "fork.png", menuRequest);
        product.setName("FORK");
        productList.add(product);

        product = createProduct( MenuType.WAITERREQUEST, "knife.png", menuRequest);
        product.setName("KNIFE");
        productList.add(product);

        product = createProduct( MenuType.WAITERREQUEST, "salt.png", menuRequest);
        product.setName("SALT");
        productList.add(product);

        product = createProduct( MenuType.WAITERREQUEST, "sauce.png", menuRequest);
        product.setName("SAUCE");
        productList.add(product);

        product = createProduct( MenuType.WAITERREQUEST, "sugar.png", menuRequest);
        product.setName("SUGAR");
        productList.add(product);

        product = createProduct( MenuType.WAITERCALL, "sugar.png", menuRequest);
        product.setName("CALL WAITER");
        productList.add(product);


        if (adminRepository.findAll().size() == 0) {
            LOGGER.info("READY!...Populating database...");

            // create client and guest
            LOGGER.info("Creating default client and guest");
            SignUpForm client = new SignUpForm("client@mail.com", "123456", "5147887884", "client");
            SignUpForm guest = new SignUpForm("guest@mail.com", "123456", "5147887884", "guest");
            SignUpForm owner = new SignUpForm("owner@mail.com", "123456", "5147887884", "owner");
            SignUpForm admin = new SignUpForm("admin", "123456", "5147887884", "admin");

            authentificationService.registerUser(client);
            authentificationService.registerUser(guest);
            authentificationService.registerUser(owner);
            authentificationService.registerUser(admin);
        }

        RestaurantDTO restaurantDTO = kitchenService.createRestaurant("owner@mail.com", "le monde chico", 5);
        restaurant = restaurantRepository.findById(restaurantDTO.getId()).get();

        // add waiter and cook to restaurant
        RestaurantEmployerDTO waiter = new RestaurantEmployerDTO(null,"waiter@mail.com","123456",restaurant.getId(),RoleName.ROLE_WAITER.toString());
        RestaurantEmployerDTO cook = new RestaurantEmployerDTO(null,"cook@mail.com","123456",restaurant.getId(),RoleName.ROLE_COOK.toString());
//        kitchenService.addUserToRestaurant(cook);
//        kitchenService.addUserToRestaurant(waiter);

        restaurant.setBill(new ArrayList<>());
        restaurant.setName("le resto chico");
        restaurant.setImgFile(getImgFile("download.jpg"));
        /*
        for (Product x : productList) {
            x.setMenu(restaurant.getMenus());
        }
        restaurant.getMenus().setProducts(productList);
        */
        restaurant.getBill().add(bill);
        List<Menu> allMenus = new ArrayList<>();
        allMenus.add(menuDejeuner);
        allMenus.add(menuDiner);
        allMenus.add(menuSouper);
        allMenus.add(menuRequest);
        restaurant.setMenus(allMenus);
        restaurantRepository.save(restaurant);
        restaurant = restaurantRepository.findById(restaurantDTO.getId()).get();
        //LOGGER.info("Restaurant menuid: " + restaurant.getMenus().getId());
        restaurant = new Restaurant();
        RestaurentTable restaurentTable = new RestaurentTable();
        restaurant.setRestaurentTables(new ArrayList<>());
        restaurant.getRestaurentTables().add(restaurentTable);
        restaurantRepository.save(restaurant);
        //restaurant + menu pour ajout/delete de produit

        restaurant = new Restaurant();
        Menu menuSupp = createMenu("Dejeuner",MenuType.FOOD);
        product = createProduct( null, "download.jpg",menuSupp);
        restaurant.setMenus(Collections.singletonList(menuSupp));
        restaurantRepository.save(restaurant);
        System.out.println("APPLICATION IS READY!!!");
    }

    private Menu createMenu(String nom, MenuType menuType) {
        Menu menuRequest = new Menu();
        menuRequest.setProducts(new ArrayList<>());
        menuRequest.setName(nom);
        menuRequest.setMenuType(menuType);
        return menuRequest;
    }

    private Product createProduct(MenuType menuType, String fileToCopy, Menu menu) throws IOException {
        Product product = new Product();
        product.setName("le steak chico");
        product.setPrix(29.99);
        product.setTempsDePreparation(30);

        ImgFile img = getImgFile(fileToCopy);
        img.setFileType("image");
        img.setProduct(product);
        product.setImgFile(img);
        product.setMenuType(menuType);
        // product.setImgUrl(serverPort+absolutePath+1);
        product.setDescription("cest bon cest bon cest bon");
        product.setOrderItems(new ArrayList<>());
        product.setCheckItems(new ArrayList<>());
        CheckItem productCheckItem = new CheckItem();
        productCheckItem.setName("fromage bleu");
        productCheckItem.setPrix(5);
        product.getCheckItems().add(productCheckItem);
        CheckItem productCheckItem2 = new CheckItem();
        productCheckItem.setName("Miel");
        productCheckItem.setPrix(10);
        product.getCheckItems().add(productCheckItem2);
        Option option = new Option();
        option.setName("Cuisson");
        option.setCheckItemList(new ArrayList<>());
        CheckItem checkItem = new CheckItem();
        checkItem.setName("moyen");
        checkItem.setPrix(5.00);
        option.getCheckItemList().add(checkItem);
        CheckItem checkItem2 = new CheckItem();
        checkItem2.setName("faible");
        checkItem2.setPrix(5.00);
        option.getCheckItemList().add(checkItem2);
        CheckItem checkItem3 = new CheckItem();
        checkItem3.setName("fort");
        checkItem3.setPrix(5.00);
        option.getCheckItemList().add(checkItem3);
        product.setOptions(new ArrayList<>());
        product.getOptions().add(option);
        if (Objects.nonNull(menu)) {
            menu.getProducts().add(product);
            product.setMenu(menu);
        }
        return product;
    }

    private ImgFile getImgFile(String fileToCopy) throws IOException {
        String pathDansProjet = fileBasePath + fileToCopy;
        Path currentRelativePath = Paths.get("");
        String absolutePath = currentRelativePath.toAbsolutePath().toString();
        InputStream is = resourceLoader.getResource(
                "classpath:img/" + fileToCopy).getInputStream();
        ImgFile img = new ImgFile();
        img.setData(IOUtils.toByteArray(is));
        return img;
    }

    public OrderItem createOrderItem(ProgressStatus progressStatus, MenuType menuType, Product product) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderStatus(progressStatus);
        orderItem.setMenuType(menuType);
        orderItem.setProduct(product);
        orderItem.setTempsDePreparation(new Date(System.currentTimeMillis()));
        return orderItem;
    }
}
