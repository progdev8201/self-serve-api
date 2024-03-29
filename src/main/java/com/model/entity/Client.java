package com.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@PrimaryKeyJoinColumn(referencedColumnName = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Client  extends  Guest implements Serializable {
    private String telephone;


    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Client(String username, String password, String telephone, String role) {
        super(username, password,role);
        this.telephone = telephone;
    }

    public Client(Guest user,String telephone){
        super(user.getUsername(),user.getPassword(), user.getRole());
        this.telephone = telephone;
    }


    public Client(){

    }
}
