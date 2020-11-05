package com.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@PrimaryKeyJoinColumn(referencedColumnName = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Owner extends Guest  implements Serializable {
    @OneToMany(cascade ={CascadeType.PERSIST, CascadeType.MERGE})
    private List<Restaurant> restaurantList;

    private String stripeAccountId;

    private String stripeCustomerId;

    @OneToOne(cascade =CascadeType.PERSIST)
    private SubscriptionEntity subscriptionEntity;

    public Owner(Guest user) {
        super(user.getUsername(), user.getPassword(), user.getRoles());
        this.restaurantList = new ArrayList<>();
    }

    public Owner() {
    }

    public SubscriptionEntity getSubscriptionEntity() {
        return subscriptionEntity;
    }

    public void setSubscriptionEntity(SubscriptionEntity subscriptionEntity) {
        this.subscriptionEntity = subscriptionEntity;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public List<Restaurant> getRestaurantList() {
        return restaurantList;
    }

    public void setRestaurantList(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    public String getStripeAccountId() {
        return stripeAccountId;
    }

    public void setStripeAccountId(String stripeAccountId) {
        this.stripeAccountId = stripeAccountId;
    }

}
