package com.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import java.util.ArrayList;
import java.util.List;

@Entity
@PrimaryKeyJoinColumn(referencedColumnName = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Owner extends Guest {
    @OneToMany
    private List<Restaurant> restaurantList;

    public Owner(Guest user) {
        super(user.getUsername(), user.getPassword(), user.getRoles());
        this.restaurantList = new ArrayList<>();
    }

    public Owner() {
    }

    public List<Restaurant> getRestaurantList() {
        return restaurantList;
    }

    public void setRestaurantList(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    @Override
    public String toString() {
        return "Owner{" +
                "restaurantList=" + restaurantList +
                '}';
    }
}
