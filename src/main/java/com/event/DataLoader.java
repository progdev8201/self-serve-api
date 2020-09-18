package com.event;

import com.model.dto.SignUpForm;
import com.model.entity.*;
import com.model.enums.ProductMenuType;
import com.model.enums.RoleName;
import com.repository.*;
import com.service.AuthentificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

@Order(3)
@Component
public class DataLoader implements CommandLineRunner {
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
    AuthentificationService authentificationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoader.class);

    @Override
    public void run(String... args) throws Exception {
        createAccount();
    }

    private void createAccount() {
        //create restaurant
        Restaurant restaurant = new Restaurant();
        restaurant.setName("le resto chico");
        restaurantRepository.save(restaurant);

        //create product list
        List<Product> productList = new ArrayList<>();
        Product product = new Product();
        product.setName("le steak chico");
        product.setPrix(29.99);
        product.setTempsDePreparation(30);
        product.setProductMenuType(ProductMenuType.DINER);
        product.setDescription("cest bon cest bon cest bon");
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
        productList.add(product);
        Product product2 = new Product();
        product2.setProductMenuType(ProductMenuType.DEJEUNER);
        product2.setName("le steak chico2");
        product2.setDescription("cest po bon cest po bon cest po bon");
        product2.setPrix(29.99);
        option = new Option();
        option.setName("Cuisson");
        option.setCheckItemList(new ArrayList<>());
        checkItem = new CheckItem();
        checkItem.setName("moyen");
        option.getCheckItemList().add(checkItem);
        checkItem2 = new CheckItem();
        checkItem2.setName("faible");
        option.getCheckItemList().add(checkItem2);
        checkItem3 = new CheckItem();
        checkItem3.setName("fort");
        option.getCheckItemList().add(checkItem3);
        product2.setOptions(new ArrayList<>());
        product2.getOptions().add(option);
        productList.add(product2);

        //create menu
        Menu menu = new Menu();
        menu.setProducts(productList);
        product.setMenu(menu);
        product2.setMenu(menu);

        RestaurentTable restaurentTable = new RestaurentTable();
        restaurentTable.setTableNumber(1);

        restaurant = new Restaurant();
        restaurant.setName("le resto chico");
        // menu=menuRepository.save(menu);
        menu.setRestaurant(restaurant);
        restaurant.setMenu(menu);
        restaurant.setRestaurentTables(new ArrayList<>());
        restaurant.getRestaurentTables().add(restaurentTable);
        restaurant = restaurantRepository.save(restaurant);


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
            authentificationService.registerUser(client);
            authentificationService.registerUser(guest);

            System.out.println("APPLICATION IS READY!!!");

        }
    }
}
