package com.model.entity;


import com.model.enums.MenuType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Menu implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String omnivoreMenuId;

    private String omnivoreMenuType;

    @OneToMany(cascade ={CascadeType.PERSIST, CascadeType.MERGE})
    private List<Product> products;

    @ManyToOne(cascade ={CascadeType.PERSIST, CascadeType.MERGE})
    private Restaurant restaurant;

    @Enumerated(EnumType.STRING)
    @Column(length = 60)
    private MenuType menuType;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MenuType getMenuType() {
        return menuType;
    }

    public void setMenuType(MenuType menuType) {
        this.menuType = menuType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getOmnivoreMenuId() {
        return omnivoreMenuId;
    }

    public void setOmnivoreMenuId(String omnivoreMenuId) {
        this.omnivoreMenuId = omnivoreMenuId;
    }

    public String getOmnivoreMenuType() {
        return omnivoreMenuType;
    }

    public void setOmnivoreMenuType(String omnivoreType) {
        this.omnivoreMenuType = omnivoreType;
    }
}
