package com.model.dto;

import java.util.List;

public class MenuDTO {
    private Long id;
    private List<ProductDTO> products;

    private RestaurantDTO restaurant;

    private List<ProductDTO> speciaux;

    private List<ProductDTO> featured;

    private List<ProductDTO> diner;

    private List<ProductDTO> dejeuner;

    private List<ProductDTO> souper;

    public List<ProductDTO> getDiner() {
        return diner;
    }

    public void setDiner(List<ProductDTO> diner) {
        this.diner = diner;
    }

    public List<ProductDTO> getDejeuner() {
        return dejeuner;
    }

    public void setDejeuner(List<ProductDTO> dejeuner) {
        this.dejeuner = dejeuner;
    }

    public List<ProductDTO> getSouper() {
        return souper;
    }

    public void setSouper(List<ProductDTO> souper) {
        this.souper = souper;
    }

    public List<ProductDTO> getFeatured() {
        return featured;
    }

    public void setFeatured(List<ProductDTO> featured) {
        this.featured = featured;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<ProductDTO> getSpeciaux() {
        return speciaux;
    }

    public void setSpeciaux(List<ProductDTO> speciaux) {
        this.speciaux = speciaux;
    }

    @Override
    public String toString() {
        return "MenuDTO{" +
                "id=" + id +
                ", products=" + products +
                ", restaurant=" + restaurant +
                ", speciaux=" + speciaux +
                '}';
    }
}
