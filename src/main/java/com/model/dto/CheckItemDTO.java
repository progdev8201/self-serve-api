package com.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;

public class CheckItemDTO implements Serializable {
    private Long id;

    private String name;

    private OptionDTO option;

    private BigDecimal prix;

    @JsonProperty("isActive")
    private boolean isActive;

    public CheckItemDTO(){

    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
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

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public OptionDTO getOption() {
        return option;
    }

    public void setOption(OptionDTO option) {
        this.option = option;
    }



    @Override
    public String toString() {
        return "CheckItemDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", option=" + option +
                '}';
    }
}
