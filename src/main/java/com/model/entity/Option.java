package com.model.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<CheckItem> checkItemList;

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

    public List<CheckItem> getCheckItemList() {
        return checkItemList;
    }

    public void setCheckItemList(List<CheckItem> checkItemList) {
        this.checkItemList = checkItemList;
    }
}
