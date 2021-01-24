package com.model.dto.requests;

import com.model.enums.MenuType;
import com.model.enums.RestaurantType;

public class CreateRestaurantRequestDTO {
    private String ownerUsername;
    private String restaurantName;
    private int nombreDeTables;
    private RestaurantType restaurantType;

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public int getNombreDeTables() {
        return nombreDeTables;
    }

    public void setNombreDeTables(int nombreDeTables) {
        this.nombreDeTables = nombreDeTables;
    }

    public RestaurantType getRestaurantType() {
        return restaurantType;
    }

    public void setRestaurantType(RestaurantType restaurantType) {
        this.restaurantType = restaurantType;
    }
}
