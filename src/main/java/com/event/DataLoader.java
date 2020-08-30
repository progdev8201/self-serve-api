package com.event;

import com.model.entity.*;
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

    private void createAccount(){
        Client client = new Client();
        client.setUsername("client1");
        Guest guest = new Guest();
        guest.setUsername("user1");
        Restaurant restaurant = new Restaurant();
        restaurant.setName("le resto chico");
        restaurantRepository.save(restaurant);
        List<Product> productList = new ArrayList<>();
        Product product = new Product();
        product.setName("le steak chico");
        product.setPrix(29.99);
        productList.add(product);
        product=productRepository.save(product);
        Product product2 = new Product();
        product2.setName("le steak chico2");
        product2.setPrix(29.99);
        productList.add(product2);
        product2=productRepository.save(product2);
        Menu menu = new Menu();
        menu.setRestaurant(restaurant);
        menu.setProducts(productList);
        menu =menuRepository.save(menu);
        product.setMenu(menu);
        productRepository.save(product);
        productRepository.save(product2);

        guestRepository.save(client);
        guestRepository.save(guest);
        if (roleRepository.findAll().size() == 0){
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

       /*     LOGGER.info("Creating default client");
            SignUpForm client = new SignUpForm("Aissatou","Diakite","677 berverly street","5147886958","client","123456","jekoum@mail.com","123456","5147845789","client");
            authentificationService.registerUser(client);*/
            System.out.println("APPLICATION IS READY!!!");

        }
    }
}
