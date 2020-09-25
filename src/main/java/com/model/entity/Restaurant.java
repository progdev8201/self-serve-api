package com.model.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Bill> bill;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Owner owner;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List <RestaurentTable> restaurentTables;

    @OneToOne
    private CustomProprety customProprety;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Menu menu;

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

    public CustomProprety getCustomProprety() {
        return customProprety;
    }

    public void setCustomProprety(CustomProprety customProprety) {
        this.customProprety = customProprety;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public List<RestaurentTable> getRestaurentTables() {
        return restaurentTables;
    }

    public void setRestaurentTables(List<RestaurentTable> restaurentTables) {
        this.restaurentTables = restaurentTables;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", billList=" + bill +
                ", owner=" + owner +
                ", customProprety=" + customProprety +
                ", menu=" + menu +
                '}';
    }
}
