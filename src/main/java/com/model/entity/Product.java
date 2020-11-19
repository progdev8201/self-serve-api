package com.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.model.enums.ProductMenuType;
import com.model.enums.ProductType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String description;

    private double prix;
    ///en minutes
    private int tempsDePreparation;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Menu menu;

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE})
    private List<Option> options;

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE})
    private List <OrderItem> orderItems;

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE})
    private List <CheckItem> checkItems;


    private String imgUrl;

    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.REMOVE, CascadeType.MERGE})
    private ImgFile imgFile;


    @OneToMany(cascade ={CascadeType.PERSIST, CascadeType.MERGE})
    private List<Rate> rates;

    @Enumerated(EnumType.STRING)
    @Column(length = 60)
    private ProductType productType;

    @Enumerated(EnumType.STRING)
    @Column(length = 60)
    private ProductMenuType productMenuType;

    public List<CheckItem> getCheckItems() {
        return checkItems;
    }

    public void setCheckItems(List<CheckItem> checkItems) {
        this.checkItems = checkItems;
    }

    public Product(long id, String name, String description, Menu menu, List<Option> options, List<OrderItem> orderItems, double prix, int tempsDePreparation, String imgUrl, ImgFile imgFile, List<Rate> rates, ProductType productType, ProductMenuType productMenuType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.menu = menu;
        this.options = options;
        this.orderItems = orderItems;
        this.prix = prix;
        this.tempsDePreparation = tempsDePreparation;
        this.imgUrl = imgUrl;
        this.imgFile = imgFile;
        this.rates = rates;
        this.productType = productType;
        this.productMenuType = productMenuType;
    }

    public Product() {
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public ImgFile getImgFile() {
        return imgFile;
    }

    public void setImgFile(ImgFile imgFile) {
        this.imgFile = imgFile;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

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
}
