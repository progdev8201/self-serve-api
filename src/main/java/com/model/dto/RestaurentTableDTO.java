package com.model.dto;

import java.util.List;

public class RestaurentTableDTO {
    private Long id;

    private int tableNumber;

    private Long omnivoreTableID;

    private Long seats;

    private List<BillDTO> bills;

    private MenuDTO menuDTO;

    private List<RestaurantDTO> restaurant;

    private ImgFileDTO imgFileDTO;

    public ImgFileDTO getImgFileDTO() {
        return imgFileDTO;
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

    public void setImgFileDTO(ImgFileDTO imgFileDTO) {
        this.imgFileDTO = imgFileDTO;
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

    public List<BillDTO> getBills() {
        return bills;
    }

    public void setBills(List<BillDTO> bills) {
        this.bills = bills;
    }

    public MenuDTO getMenuDTO() {
        return menuDTO;
    }

    public void setMenuDTO(MenuDTO menuDTO) {
        this.menuDTO = menuDTO;
    }

    public List<RestaurantDTO> getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(List<RestaurantDTO> restaurant) {
        this.restaurant = restaurant;
    }
}
