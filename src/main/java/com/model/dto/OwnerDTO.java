package com.model.dto;

import java.util.List;

public class  OwnerDTO extends GuestDTO{
    private List<RestaurantDTO> restaurants;

    public List<RestaurantDTO> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<RestaurantDTO> restaurants) {
        this.restaurants = restaurants;
    }

    @Override
    public String toString() {
        return "OwnerDTO{" +
                "restaurantList=" + restaurants +
                '}';
    }
}
