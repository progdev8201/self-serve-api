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
public class Waiter extends Guest implements Serializable {
    @OneToOne
    private Restaurant restaurant;

    public Waiter(String username, String password, String role,Restaurant restaurant) {
        super(username, password, role);
        this.restaurant = restaurant;
    }

    public Waiter(RestaurantUserDto restaurantUserDto){
        super(restaurantUserDto.getUsername(),restaurantUserDto.getPassword(),restaurantUserDto.getRole().toString());
    }

    public Waiter() {
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
