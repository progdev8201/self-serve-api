package com.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.model.dto.RestaurantEmployerDTO;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import java.io.Serializable;

@Entity
@PrimaryKeyJoinColumn(referencedColumnName = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Waiter extends Employer {
    public Waiter(String username, String password, String role, Restaurant restaurant) {
        super(username, password, role, restaurant);
    }

    public Waiter(RestaurantEmployerDTO employer){
        super(employer.getUsername(), employer.getPassword(), employer.getRole().toString(),null);
    }

    public Waiter() {
    }

}
