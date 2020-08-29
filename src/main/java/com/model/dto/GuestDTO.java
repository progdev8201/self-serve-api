package com.model.dto;

import com.model.entity.Bill;
import com.model.entity.Role;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GuestDTO {
    private Long id;
    protected String username;

    protected String password;

    private List<BillDTO> bills;

    protected Set<Role> roles = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<BillDTO> getBills() {
        return bills;
    }

    public void setBills(List<BillDTO> bills) {
        this.bills = bills;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
