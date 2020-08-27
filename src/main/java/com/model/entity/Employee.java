package com.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(referencedColumnName = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Employee extends Guest {
    public Employee(Guest user) {
        super(user.getUsername(), user.getPassword(), user.getRoles());
    }

    public Employee() {
    }
}
