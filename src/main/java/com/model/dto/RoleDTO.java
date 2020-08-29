package com.model.dto;

import com.model.entity.Bill;
import com.model.entity.CustomProprety;
import com.model.entity.Menu;
import com.model.entity.Owner;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

public class RoleDTO {
    private Long id;

    private String name;

    private List<BillDTO> billList;

    private OwnerDTO owner;

    private CustomPropretyDTO customProprety;

    private MenuDTO menu;

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

    public List<BillDTO> getBillList() {
        return billList;
    }

    public void setBillList(List<BillDTO> billList) {
        this.billList = billList;
    }

    public OwnerDTO getOwner() {
        return owner;
    }

    public void setOwner(OwnerDTO owner) {
        this.owner = owner;
    }

    public CustomPropretyDTO getCustomProprety() {
        return customProprety;
    }

    public void setCustomProprety(CustomPropretyDTO customProprety) {
        this.customProprety = customProprety;
    }

    public MenuDTO getMenu() {
        return menu;
    }

    public void setMenu(MenuDTO menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "RoleDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", billList=" + billList +
                ", owner=" + owner +
                ", customProprety=" + customProprety +
                ", menu=" + menu +
                '}';
    }
}
