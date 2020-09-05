package com.model.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class RestaurentTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int tableNumber;

    @OneToMany
    private List<Bill> bill;

    @ManyToOne
    private RestaurentTable restaurentTable;


    public RestaurentTable getRestaurentTable() {
        return restaurentTable;
    }

    public void setRestaurentTable(RestaurentTable restaurentTable) {
        this.restaurentTable = restaurentTable;
    }

    public List<Bill> getBill() {
        return bill;
    }

    public void setBill(List<Bill> bill) {
        this.bill = bill;
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
