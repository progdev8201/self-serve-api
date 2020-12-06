package com.model.dto;

import com.model.enums.MenuType;

import java.util.List;

public class ProductDTO {

    private long id;

    private String name;

    private String description;

    private MenuDTO menu;

    private List<OptionDTO> options;

    private double prix;

    private List<CheckItemDTO> checkItems;

    ///en minutes
    private int tempsDePreparation;

    private String imgUrl;

    private List<RateDTO> rates;

    private MenuType menuType;

    private ImgFileDTO imgFileDTO;


    public List<CheckItemDTO> getCheckItems() {
        return checkItems;
    }

    public void setCheckItems(List<CheckItemDTO> checkItems) {
        this.checkItems = checkItems;
    }

    public ImgFileDTO getImgFileDTO() {
        return imgFileDTO;
    }

    public void setImgFileDTO(ImgFileDTO imgFileDTO) {
        this.imgFileDTO = imgFileDTO;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public MenuType getMenuType() {
        return menuType;
    }

    public void setMenuType(MenuType menuType) {
        this.menuType = menuType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MenuDTO getMenu() {
        return menu;
    }

    public void setMenu(MenuDTO menu) {
        this.menu = menu;
    }

    public List<OptionDTO> getOptions() {
        return options;
    }

    public void setOptions(List<OptionDTO> options) {
        this.options = options;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getTempsDePreparation() {
        return tempsDePreparation;
    }

    public void setTempsDePreparation(int tempsDePreparation) {
        this.tempsDePreparation = tempsDePreparation;
    }

    public List<RateDTO> getRates() {
        return rates;
    }

    public void setRates(List<RateDTO> rates) {
        this.rates = rates;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", menu=" + menu +
                ", options=" + options +
                ", prix=" + prix +
                ", tempsDePreparation=" + tempsDePreparation +
                ", rates=" + rates +
                '}';
    }
}
