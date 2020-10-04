package com.model.dto;


import com.mapping.RestaurentTableToRestaurenTableDTO;
import com.model.entity.RestaurentTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RestaurantSelectionDTO implements Serializable {
    private Long restaurantId;
    private Long menuId;
    private String restaurantName;
    private List<RestaurentTableDTO> restaurentTablesDTO;

    public RestaurantSelectionDTO() {
    }

    public RestaurantSelectionDTO(Long restaurantId ,Long menuId, String restaurantName,List<RestaurentTable> restaurentTables) {
        this.restaurantId = restaurantId;
        this.menuId = menuId;
        this.restaurantName = restaurantName;
        restaurentTablesDTO = new ArrayList<>();

        restaurentTables.forEach(restaurentTable -> {
            restaurentTablesDTO.add(RestaurentTableToRestaurenTableDTO.instance.convert(restaurentTable));
        });
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

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
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
                "menuId=" + menuId +
                ", restaurantName='" + restaurantName + '\'' +
                '}';
    }
}
