package com.model.dto;

import com.model.entity.Cook;
import com.model.entity.Waiter;
import com.model.enums.RoleName;

import java.io.Serializable;

public class RestaurantUserDto implements Serializable {
    private Long id;
    private String username;
    private String password;
    private Long restaurantId;
    private RoleName role;

    public RestaurantUserDto() {
    }

    public RestaurantUserDto(Long id, String username, String password, Long restaurantId, RoleName role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.restaurantId = restaurantId;
        this.role = role;
    }
     public RestaurantUserDto(Cook cook) {
        this.id = cook.getId();
        this.username = cook.getUsername();
        this.password = cook.getPassword();
        this.restaurantId = cook.getRestaurant().getId();
        this.role = RoleName.ROLE_COOK;
    }
     public RestaurantUserDto(Waiter waiter) {
        this.id = waiter.getId();
        this.username = waiter.getUsername();
        this.password = waiter.getPassword();
        this.restaurantId = waiter.getRestaurant().getId();
        this.role = RoleName.ROLE_WAITER;
    }

    public RoleName getRole() {
        return role;
    }

    public void setRole(RoleName role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Override
    public String toString() {
        return "RestaurantUserDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", restaurantId=" + restaurantId +
                ", role=" + role +
                '}';
    }
}
