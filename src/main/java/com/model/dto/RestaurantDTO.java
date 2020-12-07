package com.model.dto;

import com.model.entity.ImgFile;

import java.util.List;

public class RestaurantDTO {
    private Long id;

    private String name;

    private List<BillDTO> bill;

    private OwnerDTO owner;

    private CustomPropretyDTO customProprety;

    private List<MenuDTO> menus;

    private List<RestaurentTableDTO> restaurentTables;

    private ImgFileDTO imgFile;

    public ImgFileDTO getImgFile() {
        return imgFile;
    }

    public void setImgFile(ImgFileDTO imgFile) {
        this.imgFile = imgFile;
    }

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

    public List<MenuDTO> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuDTO> menus) {
        this.menus = menus;
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
                ", restaurentTablesDTO=" + restaurentTables +
                '}';
    }
}
