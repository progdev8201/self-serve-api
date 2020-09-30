package com.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import java.util.List;
import java.util.Set;

@Entity
@PrimaryKeyJoinColumn(referencedColumnName = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Client  extends  Guest{
    private String telephone;


    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Client(String username, String password, String telephone, Set<Role> roles) {
        super(username, password,roles);
        this.telephone = telephone;
    }

    public Client(Guest user,String telephone){
        super(user.getUsername(),user.getPassword(), user.getRoles());
        this.telephone = telephone;
    }


    public Client(){

    }
}
