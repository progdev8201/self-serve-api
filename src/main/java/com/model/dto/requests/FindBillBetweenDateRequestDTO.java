package com.model.dto.requests;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FindBillBetweenDateRequestDTO implements Serializable {
    private LocalDateTime begin;
    private LocalDateTime end;
    private Long restaurantId;

    public LocalDateTime getBegin() {
        return begin;
    }

    public void setBegin(LocalDateTime begin) {
        this.begin = begin;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

}
