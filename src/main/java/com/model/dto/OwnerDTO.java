package com.model.dto;

import com.model.entity.Restaurant;

import java.util.List;

public class  OwnerDTO extends GuestDTO{
    private List<RestaurantDTO> restaurantList;

    public List<RestaurantDTO> getRestaurantList() {
        return restaurantList;
    }

    public void setRestaurantList(List<RestaurantDTO> restaurantList) {
        this.restaurantList = restaurantList;
    }

    @Override
    public String toString() {
        return "OwnerDTO{" +
                "restaurantList=" + restaurantList +
                '}';
    }
}
