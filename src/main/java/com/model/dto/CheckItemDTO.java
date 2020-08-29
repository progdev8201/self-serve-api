package com.model.dto;

import com.model.entity.Option;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

public class CheckItemDTO {
    private Long id;

    private String name;

    private OptionDTO option;

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
