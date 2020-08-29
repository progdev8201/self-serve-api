package com.model.dto;

import com.model.enums.BillStatus;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

public class BillDTO {
    private Long id;
    private List<OrderItemDTO> orderItems;

    private GuestDTO orderCustomer;

    private LocalDate date;

    private double prixTotal;

    @Enumerated(EnumType.STRING)
    //@NaturalId
    @Column(length = 60)
    private BillStatus billStatus;

    @ManyToOne
    private RestaurantDTO restaurant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }

    public GuestDTO getOrderCustomer() {
        return orderCustomer;
    }

    public void setOrderCustomer(GuestDTO orderCustomer) {
        this.orderCustomer = orderCustomer;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(double prixTotal) {
        this.prixTotal = prixTotal;
    }

    public BillStatus getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(BillStatus billStatus) {
        this.billStatus = billStatus;
    }

    public RestaurantDTO getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantDTO restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "BillDTO{" +
                "id=" + id +
                ", orderItems=" + orderItems +
                ", orderCustomer=" + orderCustomer +
                ", date=" + date +
                ", prixTotal=" + prixTotal +
                ", billStatus=" + billStatus +
                ", restaurant=" + restaurant +
                '}';
    }
}
