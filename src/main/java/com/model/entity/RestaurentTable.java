package com.model.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class RestaurentTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int tableNumber;

    //Todo: rajouter un s a bill
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Bill> bills;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Restaurant restaurant;


    public Restaurant getRestaurentTable() {
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
