package com.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import java.io.Serializable;

@Entity
@PrimaryKeyJoinColumn(referencedColumnName = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Waiter extends Guest implements Serializable {
    public Waiter(Guest user) {
        super(user.getUsername(), user.getPassword(), user.getRoles());
    }

    public Waiter() {
    }
}
