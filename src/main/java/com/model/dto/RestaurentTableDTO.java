package com.model.dto;

import com.model.entity.Menu;
import com.model.entity.RestaurentTable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

public class RestaurentTableDTO {
    private Long id;

    private int tableNumber;

    private List<BillDTO> billDTOList;

    private MenuDTO menuDTO;

    private List <RestaurentTableDTO> restaurentTableDTOS;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public List<BillDTO> getBillDTOList() {
        return billDTOList;
    }

    public void setBillDTOList(List<BillDTO> billDTOList) {
        this.billDTOList = billDTOList;
    }

    public MenuDTO getMenuDTO() {
        return menuDTO;
    }

    public void setMenuDTO(MenuDTO menuDTO) {
        this.menuDTO = menuDTO;
    }

    public List<RestaurentTableDTO> getRestaurentTableDTOS() {
        return restaurentTableDTOS;
    }

    public void setRestaurentTableDTOS(List<RestaurentTableDTO> restaurentTableDTOS) {
        this.restaurentTableDTOS = restaurentTableDTOS;
    }
}
