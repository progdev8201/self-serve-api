package com.model.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Menu implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade ={CascadeType.PERSIST})
    private List<Product> products;

    @OneToMany(cascade ={CascadeType.PERSIST, CascadeType.MERGE})
    private List<Product> speciaux;

    @OneToOne(cascade ={CascadeType.PERSIST, CascadeType.MERGE})
    private Restaurant restaurant;

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
}
