package com.model.dto;

public class CookDTO extends GuestDTO {
    private RestaurantDTO restaurantDTO;

    public RestaurantDTO getRestaurantDTO() {
        return restaurantDTO;
    }

    public void setRestaurantDTO(RestaurantDTO restaurantDTO) {
        this.restaurantDTO = restaurantDTO;
    }
}
