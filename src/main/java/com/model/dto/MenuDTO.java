package com.model.dto;

import com.model.entity.Product;
import com.model.entity.Restaurant;
import com.model.enums.MenuType;

import javax.persistence.*;
import java.util.List;

public class MenuDTO {
    private Long id;

    private List<ProductDTO> products;

    private RestaurantDTO restaurant;

    private String name;

    private MenuType menuType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MenuType getMenuType() {
        return menuType;
    }

    public void setMenuType(MenuType menuType) {
        this.menuType = menuType;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }

    public RestaurantDTO getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantDTO restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "MenuDTO{" +
                "id=" + id +
                ", products=" + products +
                ", restaurant=" + restaurant +
                ", name='" + name + '\'' +
                '}';
    }
}
