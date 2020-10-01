package com.model.dto;


import java.io.Serializable;

public class RestaurantSelectionDTO implements Serializable {
    private Long restaurantId;
    private Long menuId;
    private String restaurantName;

    public RestaurantSelectionDTO() {
    }

    public RestaurantSelectionDTO(Long restaurantId ,Long menuId, String restaurantName) {
        this.restaurantId = restaurantId;
        this.menuId = menuId;
        this.restaurantName = restaurantName;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
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
