package com.model.dto;

import java.util.List;

public class RestaurantDTO {
    private Long id;

    private String name;

    private List<BillDTO> bill;

    private OwnerDTO owner;

    private CustomPropretyDTO customProprety;

    private MenuDTO menu;

    private List<RestaurentTableDTO> restaurentTables;


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

    public List<BillDTO> getBill() {
        return bill;
    }

    public void setBill(List<BillDTO> bill) {
        this.bill = bill;
    }

    public OwnerDTO getOwner() {
        return owner;
    }

    public void setOwner(OwnerDTO owner) {
        this.owner = owner;
    }

    public CustomPropretyDTO getCustomProprety() {
        return customProprety;
    }

    public void setCustomProprety(CustomPropretyDTO customProprety) {
        this.customProprety = customProprety;
    }

    public MenuDTO getMenu() {
        return menu;
    }

    public void setMenu(MenuDTO menu) {
        this.menu = menu;
    }

    public List<RestaurentTableDTO> getRestaurentTables() {
        return restaurentTables;
    }

    public void setRestaurentTables(List<RestaurentTableDTO> restaurentTables) {
        this.restaurentTables = restaurentTables;
    }

    @Override
    public String toString() {
        return "RestaurantDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", billList=" + bill +
                ", owner=" + owner +
                ", customProprety=" + customProprety +
                ", menu=" + menu +
                ", restaurentTablesDTO=" + restaurentTables +
                '}';
    }
}
