package com.model.dto;

import java.util.List;

public class  OwnerDTO extends GuestDTO{
    private List<RestaurantDTO> restaurants;

    private String stripeAccountId;

    private String stripeCustomerId;

    private Boolean isStripeEnable;

    public List<RestaurantDTO> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<RestaurantDTO> restaurants) {
        this.restaurants = restaurants;
    }

    public Boolean getStripeEnable() {
        return isStripeEnable;
    }

    public void setStripeEnable(Boolean stripeEnable) {
        isStripeEnable = stripeEnable;
    }

    public String getStripeAccountId() {
        return stripeAccountId;
    }

    public void setStripeAccountId(String stripeAccountId) {
        this.stripeAccountId = stripeAccountId;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    @Override
    public String toString() {
        return "OwnerDTO{" +
                "restaurantList=" + restaurants +
                '}';
    }
}
