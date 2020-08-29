package com.model.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany
    private List<Bill> billList;

    @ManyToOne
    private Owner owner;

    @OneToOne
    private CustomProprety customProprety;

    @OneToOne
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

    public List<Bill> getBillList() {
        return billList;
    }

    public void setBillList(List<Bill> billList) {
        this.billList = billList;
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

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", billList=" + billList +
                ", owner=" + owner +
                ", customProprety=" + customProprety +
                ", menu=" + menu +
                '}';
    }
}
