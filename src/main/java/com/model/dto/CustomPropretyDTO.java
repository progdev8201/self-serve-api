package com.model.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class CustomPropretyDTO {
    private Long id;
    private String backGroundColor;
    private String logoImage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBackGroundColor() {
        return backGroundColor;
    }

    public void setBackGroundColor(String backGroundColor) {
        this.backGroundColor = backGroundColor;
    }

    public String getLogoImage() {
        return logoImage;
    }

    public void setLogoImage(String logoImage) {
        this.logoImage = logoImage;
    }

    @Override
    public String toString() {
        return "CustomPropretyDTO{" +
                "id=" + id +
                ", backGroundColor='" + backGroundColor + '\'' +
                ", logoImage='" + logoImage + '\'' +
                '}';
    }
}
