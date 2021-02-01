package com.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.model.enums.MenuType;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String omnivoreItemId;
    
    private String orderProfileId;

    private String name;

    private String description;

    private BigDecimal prix;
    ///en minutes
    private int tempsDePreparation;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Menu menu;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
    private List<Option> options;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
    private List<OrderItem> orderItems;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
    private List<CheckItem> checkItems;


    private String imgUrl;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
    private ImgFile imgFile;


    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Rate> rates;

    @Enumerated(EnumType.STRING)
    @Column(length = 60)
    private MenuType menuType;


    public Product(long id, String name, String description, Menu menu, List<Option> options, List<OrderItem> orderItems, BigDecimal prix, int tempsDePreparation, String imgUrl, ImgFile imgFile, List<Rate> rates, MenuType menuType, String orderProfileId) {
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
        this.menuType = menuType;
        this.orderProfileId = orderProfileId;
    }

    public Product() {
    }

    public String getOmnivoreItemId() {
        return omnivoreItemId;
    }

    public void setOmnivoreItemId(String omnivoreItemId) {
        this.omnivoreItemId = omnivoreItemId;
    }

    public List<CheckItem> getCheckItems() {
        return checkItems;
    }

    public void setCheckItems(List<CheckItem> checkItems) {
        this.checkItems = checkItems;
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

    public String getOrderProfileId() {
        return orderProfileId;
    }

    public void setOrderProfileId(String orderProfileId) {
        this.orderProfileId = orderProfileId;
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

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public int getTempsDePreparation() {
        return tempsDePreparation;
    }

    public void setTempsDePreparation(int tempsDePreparation) {
        this.tempsDePreparation = tempsDePreparation;
    }

    public MenuType getMenuType() {
        return menuType;
    }

    public void setMenuType(MenuType menuType) {
        this.menuType = menuType;
    }
}
