package com.model.dto;


import com.mapping.RestaurentTableToRestaurenTableDTO;
import com.model.entity.Menu;
import com.model.entity.RestaurentTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RestaurantSelectionDTO implements Serializable {
    private Long restaurantId;
    private List<MenuDTO> menuDTOS;
    private String restaurantName;
    private List<RestaurentTableDTO> restaurentTablesDTO;

    public RestaurantSelectionDTO() {
    }

    public List<RestaurentTableDTO> getRestaurentTablesDTO() {
        return restaurentTablesDTO;
    }

    public void setRestaurentTablesDTO(List<RestaurentTableDTO> restaurentTablesDTO) {
        this.restaurentTablesDTO = restaurentTablesDTO;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public List<MenuDTO> getMenuDTOS() {
        return menuDTOS;
    }

    public void setMenuDTOS(List<MenuDTO> menuDTOS) {
        this.menuDTOS = menuDTOS;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    @Override
    public String toString() {
        return "RestaurantSelectionDTO{" +
                "restaurantId=" + restaurantId +
                ", menuDTOS=" + menuDTOS +
                ", restaurantName='" + restaurantName + '\'' +
                ", restaurentTablesDTO=" + restaurentTablesDTO +
                '}';
    }
}
