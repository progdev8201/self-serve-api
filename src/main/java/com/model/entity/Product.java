package com.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.model.enums.ProductMenuType;
import com.model.enums.ProductType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private String description;

    @ManyToOne
    private Menu menu;

    @OneToMany
    private List<Option> options;

    private double prix;
    ///en minutes
    private int tempsDePreparation;

    @OneToMany
    private List<Rate> rates;

    @Enumerated(EnumType.STRING)
    @Column(length = 60)
    private ProductType productType;

    @Enumerated(EnumType.STRING)
    @Column(length = 60)
    private ProductMenuType productMenuType;


    public ProductMenuType getProductMenuType() {
        return productMenuType;
    }

    public void setProductMenuType(ProductMenuType productMenuType) {
        this.productMenuType = productMenuType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public List<Rate> getRates() {
        return rates;
    }

    public void setRates(List<Rate> rates) {
        this.rates = rates;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getTempsDePreparation() {
        return tempsDePreparation;
    }

    public void setTempsDePreparation(int tempsDePreparation) {
        this.tempsDePreparation = tempsDePreparation;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", menu=" + menu +
                ", options=" + options +
                ", prix=" + prix +
                ", tempsDePreparation=" + tempsDePreparation +
                ", rates=" + rates +
                ", productType=" + productType +
                '}';
    }
}
