package com.model.dto;

import com.model.enums.MenuType;
import com.model.enums.ProgressStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class OrderItemDTO {
    private Long id;

    private ProductDTO product;

    private ProgressStatus orderStatus;

    private BigDecimal prix;

    private LocalDateTime delaiDePreparation;

    private List<OptionDTO> option;

    private List<CheckItemDTO> checkItems;

    private String orderProfileId;

    private boolean selected;

    private MenuType menuType;

    private String commentaires;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getOrderProfileId() {
        return orderProfileId;
    }

    public void setOrderProfileId(String orderProfileId) {
        this.orderProfileId = orderProfileId;
    }

    public String getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public ProgressStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(ProgressStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    private Date tempsDePreparation;


    public Date getTempsDePreparation() {
        return tempsDePreparation;
    }

    public void setTempsDePreparation(Date tempsDePreparation) {
        this.tempsDePreparation = tempsDePreparation;
    }

    public LocalDateTime getDelaiDePreparation() {
        return delaiDePreparation;
    }

    public void setDelaiDePreparation(LocalDateTime delaiDePreparation) {
        this.delaiDePreparation = delaiDePreparation;
    }

    public List<OptionDTO> getOption() {
        return option;
    }

    public void setOption(List<OptionDTO> option) {
        this.option = option;
    }

    public MenuType getMenuType() {
        return menuType;
    }

    public void setMenuType(MenuType menuType) {
        this.menuType = menuType;
    }

    public List<CheckItemDTO> getCheckItems() {
        return checkItems;
    }

    public void setCheckItems(List<CheckItemDTO> checkItems) {
        this.checkItems = checkItems;
    }

    @Override
    public String toString() {
        return "OrderItemDTO{" +
                "id=" + id +
                ", product=" + product +
                ", orderStatus=" + orderStatus +
                ", prix=" + prix +
                ", delaiDePreparation=" + delaiDePreparation +
                ", option=" + option +
                ", checkItems=" + checkItems +
                ", isAssigned=" + selected +
                ", menuType=" + menuType +
                ", commentaires='" + commentaires + '\'' +
                ", tempsDePreparation=" + tempsDePreparation +
                '}';
    }
}
