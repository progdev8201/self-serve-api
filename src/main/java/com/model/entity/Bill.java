package com.model.entity;

import com.model.enums.BillStatus;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<OrderItem> orderItems;

    @ManyToOne
    private Guest orderCustomer;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private RestaurentTable restaurentTable;

    private LocalDateTime date;

    private double prixTotal;

    @Enumerated(EnumType.STRING)
    //@NaturalId
    @Column(length = 60)
    private BillStatus billStatus;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Restaurant restaurant;

    public RestaurentTable getRestaurentTable() {
        return restaurentTable;
    }

    public void setRestaurentTable(RestaurentTable restaurentTable) {
        this.restaurentTable = restaurentTable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }



    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
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

    public Guest getOrderCustomer() {
        return orderCustomer;
    }

    public void setOrderCustomer(Guest orderCustomer) {
        this.orderCustomer = orderCustomer;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", orderItems=" + orderItems +
                ", orderCustomer=" + orderCustomer +
                ", restaurentTable=" + restaurentTable +
                ", date=" + date +
                ", prixTotal=" + prixTotal +
                ", billStatus=" + billStatus +
                ", restaurant=" + restaurant +
                '}';
    }
}
