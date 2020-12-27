package com.model.dto;

public class WaiterDTO extends GuestDTO {
    private RestaurantDTO restaurantDTO;

    public RestaurantDTO getRestaurantDTO() {
        return restaurantDTO;
    }

    public void setRestaurantDTO(RestaurantDTO restaurantDTO) {
        this.restaurantDTO = restaurantDTO;
    }
}
