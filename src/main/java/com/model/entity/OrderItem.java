package com.model.entity;

import com.model.enums.MenuType;
import com.model.enums.ProgressStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
public class OrderItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(length = 60)
    private ProgressStatus orderStatus;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Bill bill;

    @Enumerated(EnumType.STRING)
    @Column(length = 60)
    private MenuType menuType;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
    private List<CheckItem> checkItems;

    private String orderProfileId;

    private boolean selected;

    private String commentaires;

    private BigDecimal prix;
    //quand le plat doit etre pret
    private LocalDateTime delaiDePreparation;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Option> option;

    private int numeroTable;

    private Date tempsDePreparation;

    private LocalDateTime startTime;

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getOrderProfileId() {
        return orderProfileId;
    }

    public void setOrderProfileId(String orderProfileId) {
        this.orderProfileId = orderProfileId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public MenuType getMenuType() {
        return menuType;
    }

    public void setMenuType(MenuType menuType) {
        this.menuType = menuType;
    }

    public List<CheckItem> getCheckItems() {
        return checkItems;
    }

    public void setCheckItems(List<CheckItem> checkItems) {
        this.checkItems = checkItems;
    }

    public String getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
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

    public int getNumeroTable() {
        return numeroTable;
    }

    public void setNumeroTable(int numeroTable) {
        this.numeroTable = numeroTable;
    }

    public Date getTempsDePreparation() {
        return tempsDePreparation;
    }

    public void setTempsDePreparation(Date tempsDePreparation) {
        this.tempsDePreparation = tempsDePreparation;
    }
}


