package com.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.model.dto.RestaurantEmployerDTO;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(referencedColumnName = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Cook extends Employer {

    public Cook(String username, String password, String role, Restaurant restaurant) {
        super(username, password, role, restaurant);
    }

    public Cook(RestaurantEmployerDTO employer){
        super(employer.getUsername(), employer.getPassword(), employer.getRole().toString(),null);
    }

    public Cook() {
    }

}
