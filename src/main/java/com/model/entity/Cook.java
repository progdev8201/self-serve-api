package com.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.model.dto.RestaurantUserDto;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import java.io.Serializable;
import java.util.Set;

@Entity
@PrimaryKeyJoinColumn(referencedColumnName = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Cook extends Guest implements Serializable {
    @OneToOne
    private Restaurant restaurant;

    public Cook(String username, String password, String role,Restaurant restaurant) {
        super(username, password, role);
        this.restaurant = restaurant;
    }

    public Cook(RestaurantUserDto restaurantUserDto){
        super(restaurantUserDto.getUsername(), restaurantUserDto.getPassword(), restaurantUserDto.getRole().toString());
    }

    public Cook() {
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
