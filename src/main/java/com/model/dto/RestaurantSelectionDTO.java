package com.model.dto;


import java.io.Serializable;

public class RestaurantSelectionDTO implements Serializable {
    private Long menuId;
    private String restaurantName;

    public RestaurantSelectionDTO() {
    }

    public RestaurantSelectionDTO(Long menuId, String restaurantName) {
        this.menuId = menuId;
        this.restaurantName = restaurantName;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    @Override
    public String toString() {
        return "RestaurantSelectionDTO{" +
                "menuId=" + menuId +
                ", restaurantName='" + restaurantName + '\'' +
                '}';
    }
}
