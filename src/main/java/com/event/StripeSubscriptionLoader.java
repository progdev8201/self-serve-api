package com.event;

import com.model.entity.StripeSubscriptionProducts;
import com.repository.StripeSubscriptionProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Order(2)
@Profile("!prod")
@Component
public class StripeSubscriptionLoader implements CommandLineRunner {

    @Autowired
    private StripeSubscriptionProductRepository stripeSubscriptionProductRepository;

    private Logger logger = Logger.getLogger(StripeSubscriptionLoader.class.getName());

    @Override
    public void run(String... args) throws Exception {
        createSubscriptions();
    }

    private void createSubscriptions() {
        List<StripeSubscriptionProducts> currentSubscriptionsProducts = stripeSubscriptionProductRepository.findAll();

        if (currentSubscriptionsProducts.size() < findSubscriptions().size()){
            findMissingSubscriptions(currentSubscriptionsProducts).forEach(stripeSubscriptionProduct -> {
                stripeSubscriptionProductRepository.save(stripeSubscriptionProduct);
                logger.info(stripeSubscriptionProduct.getProductName() + " was added successfully");
            });
        }
    }

    private List<StripeSubscriptionProducts> findSubscriptions() {
        // Dine in plan premium
        StripeSubscriptionProducts stripeSubscriptionProducts = new StripeSubscriptionProducts();
        stripeSubscriptionProducts.setProductName("Dine in plan premium");
        stripeSubscriptionProducts.setProductPrice(200.00);
        stripeSubscriptionProducts.setProductDescription("No set up fee,Up to 5 restaurants, Up to 500 products,Up to 30 menus,Sale analytics (Coming Soon),E-mail support");
        stripeSubscriptionProducts.setPriceId("price_1HiTbuC5UoZOX4GRhbClArla");


        // Dine in plan basic
        StripeSubscriptionProducts stripeSubscriptionProducts2 = new StripeSubscriptionProducts();
        stripeSubscriptionProducts2.setProductName("Dine in plan basic");
        stripeSubscriptionProducts2.setProductPrice(125.00);
        stripeSubscriptionProducts2.setProductDescription("No set up fee,Up to 2 restaurants,Up to 100 products,Up to 10 menus,E-mail support");
        stripeSubscriptionProducts2.setPriceId("price_1HiTYKC5UoZOX4GRDOIONoDR");



        return Arrays.asList(stripeSubscriptionProducts,stripeSubscriptionProducts2);
    }

    private List<StripeSubscriptionProducts> findMissingSubscriptions(List<StripeSubscriptionProducts> currentSubscriptionsProducts){
        return findSubscriptions()
                .stream()
                .filter(stripeSubscriptionProducts -> !isInList(stripeSubscriptionProducts.getProductName(),currentSubscriptionsProducts))
                .collect(Collectors.toList());
    }

    private boolean isInList(String subscriptionName,List<StripeSubscriptionProducts> currentSubscriptionsProducts ){
        return currentSubscriptionsProducts
                .stream()
                .filter(subscriptionProduct -> subscriptionProduct.getProductName().equals(subscriptionName))
                .findFirst()
                .isPresent();
    }


}
