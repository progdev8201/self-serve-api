package com.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckItemDTO {
    private Long id;

    private String name;

    private OptionDTO option;

    @JsonProperty("isActive")
    private boolean isActive;

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
