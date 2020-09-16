package com.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import java.util.ArrayList;
import java.util.List;

@Entity
@PrimaryKeyJoinColumn(referencedColumnName = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Owner extends Guest {
    @OneToMany
    private List<Restaurant> restaurantList;

    private String stripeAccountId;

    public Owner(Guest user) {
        super(user.getUsername(), user.getPassword(), user.getRoles());
        this.restaurantList = new ArrayList<>();
    }

    public Owner() {
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

    @Override
    public String toString() {
        return "Owner{" +
                "restaurantList=" + restaurantList +
                ", stripeAccountId='" + stripeAccountId + '\'' +
                '}';
    }

}
