package com.model.dto;

import com.model.entity.CheckItem;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

public class OptionDTO {
    private Long id;

    private String name;

    private List<CheckItemDTO> checkItemList;

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

    public List<CheckItemDTO> getCheckItemList() {
        return checkItemList;
    }

    public void setCheckItemList(List<CheckItemDTO> checkItemList) {
        this.checkItemList = checkItemList;
    }

    @Override
    public String toString() {
        return "OptionDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", checkItemList=" + checkItemList +
                '}';
    }
}
