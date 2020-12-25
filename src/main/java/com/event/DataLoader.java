package com.event;

import com.google.zxing.WriterException;
import com.model.dto.RestaurantDTO;
import com.model.dto.RestaurantEmployerDTO;
import com.model.dto.SignUpForm;
import com.model.entity.*;
import com.model.enums.BillStatus;
import com.model.enums.MenuType;
import com.model.enums.ProgressStatus;
import com.model.enums.RoleName;
import com.repository.*;
import com.service.AuthentificationService;
import com.service.KitchenService;
import com.service.MenuCreationService;
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
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private KitchenService kitchenService;


    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    MenuCreationService menuCreationService;

    @Autowired
    private AuthentificationService authentificationService;

    @Autowired
    private StripeService stripeService;

    @Autowired
    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoader.class);

    @Override
    public void run(String... args) throws Exception {
        stripeService.initApplePay();

        if (restaurantRepository.findAll().isEmpty())
            createAccount();
    }

    private void createAccount() throws IOException, WriterException, StripeException {
        //create restaurant
        Restaurant restaurant = new Restaurant();

        restaurantRepository.save(restaurant);
        Bill bill = new Bill();
        bill.setBillStatus(BillStatus.PROGRESS);
        bill.setOrderItems(new ArrayList<>());
        Menu menuRequest;

        Menu menuDiner = menuCreationService.createMenu("Diner", MenuType.FOOD);
        Menu menuSouper = menuCreationService.createMenu("Souper", MenuType.FOOD);
        Menu menuDejeuner = menuCreationService.createMenu("Dejeuner", MenuType.FOOD);
        //todo trop long pour rien mieux de faire un loop

        for (int i =0;i<3;i++){
            menuCreationService.createProduct(MenuType.FOOD, "download.jpg", 29.99 +i, 30+i, "Steak chico dejeuner " +i, menuDejeuner);
        }

        for (int i =0;i<3;i++){
            menuCreationService.createProduct(MenuType.FOOD, "download.jpg", 29.99 +i, 30+i, "Steak chico diner " +i, menuDiner);
        }

        for (int i =0;i<3;i++){
            menuCreationService.createProduct(MenuType.FOOD, "download.jpg", 29.99 +i, 30+i, "Steak chico souper " +i, menuSouper);
        }

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
        RestaurantEmployerDTO waiter = new RestaurantEmployerDTO(null, "waiter@mail.com", "123456", restaurant.getId(), RoleName.ROLE_WAITER.toString(), "owner@mail.com");
        RestaurantEmployerDTO cook = new RestaurantEmployerDTO(null, "cook@mail.com", "123456", restaurant.getId(), RoleName.ROLE_COOK.toString(), "owner@mail.com");
        kitchenService.addUserToRestaurant(cook);
        kitchenService.addUserToRestaurant(waiter);

        restaurant.setBill(new ArrayList<>());
        restaurant.setName("le resto chico");
        restaurant.setImgFile(menuCreationService.getImgFile("download.jpg"));

        restaurant.getBill().add(bill);
        restaurant.getBill().add(new Bill());
        List<Menu> allMenus = new ArrayList<>();
        allMenus.add(menuDejeuner);
        allMenus.add(menuDiner);
        allMenus.add(menuSouper);

        restaurant.getMenus().addAll(allMenus);
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
        Menu menuSupp = menuCreationService.createMenu("Dejeuner", MenuType.FOOD);
        menuCreationService.createProduct(null, "download.jpg", 0 , 0, "Steak chico dejeuner " , menuSupp);
        restaurant.setMenus(Collections.singletonList(menuSupp));
        restaurantRepository.save(restaurant);
        System.out.println("APPLICATION IS READY!!!");
    }
}
