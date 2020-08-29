package com.model.entity;

import com.model.enums.ProgressStatus;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private Product product;

    @Enumerated(EnumType.STRING)
    //@NaturalId
    @Column(length = 60)
    private ProgressStatus orderStatus;

    private double prix;
    //quand le plat doit etre pret
    private LocalDateTime delaiDePreparation ;
    @OneToMany
    private List<Option> option;

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

    public ProgressStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(ProgressStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public LocalDateTime getDelaiDePreparation() {
        return delaiDePreparation;
    }

    public void setDelaiDePreparation(LocalDateTime delaiDePreparation) {
        this.delaiDePreparation = delaiDePreparation;
    }

    public List<Option> getOption() {
        return option;
    }

    public void setOption(List<Option> option) {
        this.option = option;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", product=" + product +
                ", orderStatus=" + orderStatus +
                ", prix=" + prix +
                ", delaiDePreparation=" + delaiDePreparation +
                ", option=" + option +
                '}';
    }
}
