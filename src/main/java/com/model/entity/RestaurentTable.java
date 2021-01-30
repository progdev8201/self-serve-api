package com.model.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class RestaurentTable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int tableNumber;

    private Long omnivoreTableID;

    private Long seats;

    //Todo: rajouter un s a bill
    @OneToMany(cascade ={CascadeType.PERSIST, CascadeType.MERGE})
    private List<Bill> bills;

    @ManyToOne(cascade ={CascadeType.PERSIST, CascadeType.MERGE})
    private Restaurant restaurant;


    @OneToOne(cascade ={CascadeType.PERSIST, CascadeType.MERGE})
    private ImgFile imgFile;

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

    public Long getOmnivoreTableID() {
        return omnivoreTableID;
    }

    public void setOmnivoreTableID(Long omnivoreTableID) {
        this.omnivoreTableID = omnivoreTableID;
    }

    public Long getSeats() {
        return seats;
    }

    public void setSeats(Long seats) {
        this.seats = seats;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public ImgFile getImgFile() {
        return imgFile;
    }

    public void setImgFile(ImgFile imgFile) {
        this.imgFile = imgFile;
    }
}
