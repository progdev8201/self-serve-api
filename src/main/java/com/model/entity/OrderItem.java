package com.model.entity;

import com.model.enums.OrderStatus;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private Product product;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private OrderStatus orderStatus;

    private double prix;
    //quand le plat doit etre pret
    private LocalDate delaiDePreparation;

    private int table;

    public int getTable() {
        return table;
    }

    public void setTable(int table) {
        this.table = table;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public LocalDate getDelaiDePreparation() {
        return delaiDePreparation;
    }

    public void setDelaiDePreparation(LocalDate delaiDePreparation) {
        this.delaiDePreparation = delaiDePreparation;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", product=" + product +
                ", orderStatus=" + orderStatus +
                ", prix=" + prix +
                ", delaiDePreparation=" + delaiDePreparation +
                ", table=" + table +
                '}';
    }
}
