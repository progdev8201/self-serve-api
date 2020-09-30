package com.model.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class RestaurentTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int tableNumber;

    //Todo: rajouter un s a bill
    @OneToMany(cascade ={CascadeType.PERSIST, CascadeType.MERGE})
    private List<Bill> bills;

    @ManyToOne(cascade ={CascadeType.PERSIST, CascadeType.MERGE})
    private Restaurant restaurant;


    @OneToOne(cascade ={CascadeType.PERSIST, CascadeType.MERGE})
    private ImgFile imgFile;

    public ImgFile getImgFile() {
        return imgFile;
    }

    public void setImgFile(ImgFile imgFile) {
        this.imgFile = imgFile;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurent) {
        this.restaurant = restaurent;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bill) {
        this.bills = bill;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }
}
