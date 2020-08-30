package com.model.entity;


import javax.persistence.*;
import java.util.List;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany
    private List<Product> products;

    @OneToMany
    private List<Product> speciaux;

    @OneToOne
    private Restaurant restaurant ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Product> getSpeciaux() {
        return speciaux;
    }

    public void setSpeciaux(List<Product> speciaux) {
        this.speciaux = speciaux;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", products=" + products +
                ", restaurant=" + restaurant +
                '}';
    }
}
