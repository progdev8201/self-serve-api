package com.event;

import com.google.zxing.WriterException;
import com.model.dto.OrderItemDTO;
import com.model.dto.RestaurantDTO;
import com.model.dto.SignUpForm;
import com.model.entity.*;
import com.model.enums.*;
import com.repository.*;
import com.service.AuthentificationService;
import com.service.KitchenService;
import com.service.StripeService;
import com.stripe.exception.StripeException;
import org.apache.commons.io.FileUtils;
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
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
    ProductRepository productRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    private KitchenService kitchenService;


    @Autowired
    ResourceLoader resourceLoader;


    @Autowired
    AuthentificationService authentificationService;

    @Autowired
    private StripeService stripeService;

    @Autowired
    private StripeSubscriptionProductRepository stripeSubscriptionProductRepository;

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


        //todo trop long pour rien mieux de faire un loop
        //create product list
        List<Product> productList = new ArrayList<>();

        Product product = createProduct(ProductMenuType.DEJEUNER, null, "download.jpg");
        orderItem = createOrderItem(ProgressStatus.READY, ProductType.SPECIAL, product);
        orderItem.setBill(bill);
        product.getOrderItems().add(orderItem);
        productList.add(product);
        bill.getOrderItems().add(orderItem);

        product = createProduct(ProductMenuType.DINER, null, "download.jpg");
        orderItem = createOrderItem(ProgressStatus.READY, ProductType.SPECIAL, product);
        orderItem.setBill(bill);
        product.getOrderItems().add(orderItem);
        productList.add(product);
        bill.getOrderItems().add(orderItem);


        product = createProduct(ProductMenuType.SOUPER, null, "download.jpg");
        orderItem = createOrderItem(ProgressStatus.PROGRESS, ProductType.WAITERREQUEST, product);
        orderItem.setBill(bill);
        product.getOrderItems().add(orderItem);
        productList.add(product);
        bill.getOrderItems().add(orderItem);

        product = createProduct(ProductMenuType.SOUPER, ProductType.SPECIAL, "download.jpg");
        orderItem = createOrderItem(ProgressStatus.PROGRESS, ProductType.WAITERREQUEST, product);
        orderItem.setBill(bill);
        product.getOrderItems().add(orderItem);
        productList.add(product);
        bill.getOrderItems().add(orderItem);

        product = createProduct(ProductMenuType.SOUPER, ProductType.SPECIAL, "download.jpg");
        productList.add(product);

        product = createProduct(ProductMenuType.SOUPER, ProductType.SPECIAL, "download.jpg");
        productList.add(product);

        product = createProduct(ProductMenuType.DINER, ProductType.CHEFCHOICE, "download.jpg");
        productList.add(product);

        product = createProduct(ProductMenuType.DINER, ProductType.CHEFCHOICE, "download.jpg");
        productList.add(product);

        product = createProduct(ProductMenuType.DINER, ProductType.CHEFCHOICE, "download.jpg");
        productList.add(product);

        product = createProduct(null, ProductType.WAITERREQUEST, "fork.png");
        product.setName("FORK");
        productList.add(product);

        product = createProduct(null, ProductType.WAITERREQUEST, "knife.png");
        product.setName("KNIFE");
        productList.add(product);

        product = createProduct(null, ProductType.WAITERREQUEST, "salt.png");
        product.setName("SALT");
        productList.add(product);

        product = createProduct(null, ProductType.WAITERREQUEST, "sauce.png");
        product.setName("SAUCE");
        productList.add(product);

        product = createProduct(null, ProductType.WAITERREQUEST, "sugar.png");
        product.setName("SUGAR");
        productList.add(product);

        product = createProduct(null, ProductType.WAITERCALL, "sugar.png");
        product.setName("CALL WAITER");
        productList.add(product);


        StripeSubscriptionProducts stripeSubscriptionProducts = new StripeSubscriptionProducts();
        stripeSubscriptionProducts.setProductName("Dine in plan premium");
        stripeSubscriptionProducts.setProductPrice(200.00);
        stripeSubscriptionProducts.setProductDescription("Unlimited orders");
        stripeSubscriptionProducts.setPriceId("price_1HiTbuC5UoZOX4GRhbClArla");
        stripeSubscriptionProductRepository.save(stripeSubscriptionProducts);

        StripeSubscriptionProducts stripeSubscriptionProducts2 = new StripeSubscriptionProducts();
        stripeSubscriptionProducts2.setProductName("Dine in plan basic");
        stripeSubscriptionProducts2.setProductPrice(125.00);

        stripeSubscriptionProducts2.setPriceId("price_1HiTYKC5UoZOX4GRDOIONoDR");
        stripeSubscriptionProducts2.setProductDescription("up to 500 orders");
        stripeSubscriptionProductRepository.save(stripeSubscriptionProducts2);

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
            SignUpForm client = new SignUpForm("client@mail.com", "123456", "5147887884", "client");
            SignUpForm guest = new SignUpForm("guest@mail.com", "123456", "5147887884", "guest");
            SignUpForm waiter = new SignUpForm("waiter@mail.com", "123456", "5147887884", "waiter");
            SignUpForm cook = new SignUpForm("cook@mail.com", "123456", "5147887884", "cook");
            SignUpForm owner = new SignUpForm("owner@mail.com", "123456", "5147887884", "owner");

            authentificationService.registerUser(client);
            authentificationService.registerUser(guest);
            authentificationService.registerUser(owner);
            authentificationService.registerUser(cook);
            authentificationService.registerUser(waiter);
        }

        RestaurantDTO restaurantDTO = kitchenService.createRestaurant("owner@mail.com", "le monde chico", 5);
        restaurant = restaurantRepository.findById(restaurantDTO.getId()).get();
        restaurant.setBill(new ArrayList<>());
        restaurant.setName("le resto chico");

        for (Product x : productList) {
            x.setMenu(restaurant.getMenu());
        }
        restaurant.getMenu().setProducts(productList);

        restaurant.getBill().add(bill);
        restaurantRepository.save(restaurant);
        restaurant = restaurantRepository.findById(restaurantDTO.getId()).get();
        LOGGER.info("Restaurant menuid: " + restaurant.getMenu().getId());
        restaurant = new Restaurant();
        RestaurentTable restaurentTable = new RestaurentTable();
        restaurant.setRestaurentTables(new ArrayList<>());
        restaurant.getRestaurentTables().add(restaurentTable);

        restaurantRepository.save(restaurant);
        System.out.println("APPLICATION IS READY!!!");
    }

    private Product createProduct(ProductMenuType productMenuType, ProductType productType, String fileToCopy) throws IOException {
        Product product = new Product();
        product.setName("le steak chico");
        product.setPrix(29.99);
        product.setTempsDePreparation(30);

        String pathDansProjet = fileBasePath + fileToCopy;
        Path currentRelativePath = Paths.get("");
        String absolutePath = currentRelativePath.toAbsolutePath().toString();
        InputStream is = resourceLoader.getResource(
                "classpath:img/" + fileToCopy).getInputStream();
        //File imgFile = new File(absolutePath + pathDansProjet);
        ImgFile img = new ImgFile();
        img.setData(IOUtils.toByteArray(is));
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

    public OrderItem createOrderItem(ProgressStatus progressStatus, ProductType productType, Product product) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderStatus(progressStatus);
        orderItem.setProductType(productType);
        orderItem.setProduct(product);
        orderItem.setTempsDePreparation(new Date(System.currentTimeMillis()));
        return orderItem;
    }
}
