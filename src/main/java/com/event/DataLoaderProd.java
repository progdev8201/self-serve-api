package com.event;

import com.google.zxing.WriterException;
import com.model.dto.SignUpForm;
import com.model.entity.*;
import com.model.enums.RoleName;
import com.repository.*;
import com.service.StripeService;
import com.stripe.exception.StripeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Order(3)
@Profile("prod")
@Component
@Transactional
public class DataLoaderProd implements CommandLineRunner {

    @Value("${config.styles.images.path}")
    private String fileBasePath;

    /*@Value("${server.port}")
    private String serverPort;
*/

    @Autowired
    RoleRepository roleRepository;


    @Autowired
    private StripeService stripeService;

    @Autowired
    private StripeSubscriptionProductRepository stripeSubscriptionProductRepository;

    @Autowired

    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoaderProd.class);

    @Override
    public void run(String... args) throws Exception {
        createAccount();
    }

    private void createAccount() throws IOException, WriterException, StripeException {
        stripeService.initApplePay();
        //create restaurant
        if(stripeSubscriptionProductRepository.findAll().size() ==0)
        {
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
        }
        if (roleRepository.findAll().size() == RoleName.values().length ) {
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
        }
    }
}
