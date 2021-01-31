package com.model.entity;

import com.model.enums.MenuType;
import com.model.enums.RestaurantType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Restaurant implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String locationId;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Bill> bill;

    @ManyToOne(cascade ={CascadeType.PERSIST, CascadeType.MERGE})
    private Owner owner;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List <RestaurentTable> restaurentTables;

    @OneToOne
    private CustomProprety customProprety;

    @OneToMany(cascade ={CascadeType.PERSIST, CascadeType.MERGE})
    private List<Menu> menus;

    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.REMOVE, CascadeType.MERGE})
    private ImgFile imgFile;

    @Enumerated(EnumType.STRING)
    @Column(length = 60)
    private RestaurantType restaurantType;

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

    public List<Bill> getBill() {
        return bill;
    }

    public void setBill(List<Bill> billList) {
        this.bill = billList;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public RestaurantType getRestaurantType() {
        return restaurantType;
    }

    public void setRestaurantType(RestaurantType restaurantType) {
        this.restaurantType = restaurantType;
    }

    public CustomProprety getCustomProprety() {
        return customProprety;
    }

    public void setCustomProprety(CustomProprety customProprety) {
        this.customProprety = customProprety;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menu) {
        this.menus = menu;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public ImgFile getImgFile() {
        return imgFile;
    }

    public void setImgFile(ImgFile imgFile) {
        this.imgFile = imgFile;
    }

    public List<RestaurentTable> getRestaurentTables() {
        return restaurentTables;
    }

    public void setRestaurentTables(List<RestaurentTable> restaurentTables) {
        this.restaurentTables = restaurentTables;
    }

}
