package com.model.omnivore;

import com.fasterxml.jackson.annotation.JsonAlias;

public class OmnivoreItem {
    private Long id;
    @JsonAlias("in_stock")
    private boolean inStock;
    private String name;
    @JsonAlias("pos_id")
    private Long posId;
    @JsonAlias("price_per_unit")
    private double pricePerUnit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPosId() {
        return posId;
    }

    public void setPosId(Long posId) {
        this.posId = posId;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }
}
