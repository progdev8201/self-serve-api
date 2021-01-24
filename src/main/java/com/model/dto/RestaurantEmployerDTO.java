package com.model.dto;

import com.model.entity.Employer;
import com.model.enums.RestaurantType;
import com.model.enums.RoleName;

import java.io.Serializable;

public class RestaurantEmployerDTO implements Serializable {
    private Long id;
    private String username;
    private String password;
    private Long restaurantId;
    private String role;
    private String ownerUsername;
    private RestaurantType restaurantType;

    public RestaurantEmployerDTO() {
    }

    public RestaurantEmployerDTO(Long id, String username, String password, Long restaurantId, String role, String ownerUsername, RestaurantType restaurantType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.restaurantId = restaurantId;
        this.role = role;
        this.ownerUsername = ownerUsername;
        this.restaurantType = restaurantType;
    }

    public RestaurantEmployerDTO(Employer employer) {
        this.id = employer.getId();
        this.username = employer.getUsername();
        this.password = employer.getPassword();
        this.restaurantId = employer.getRestaurant().getId();
        this.role = employer.getRole();
        this.ownerUsername = employer.getRestaurant().getOwner().getUsername();
        this.restaurantType = employer.getRestaurant().getRestaurantType();
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
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

    public RestaurantType getRestaurantType() {
        return restaurantType;
    }

    public void setRestaurantType(RestaurantType restaurantType) {
        this.restaurantType = restaurantType;
    }

    @Override
    public String toString() {
        return "RestaurantEmployerDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", restaurantId=" + restaurantId +
                ", role=" + role +
                '}';
    }
}
