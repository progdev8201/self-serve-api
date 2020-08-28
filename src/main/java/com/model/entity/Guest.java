package com.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@PrimaryKeyJoinColumn(referencedColumnName = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Guest  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    protected String username;

    protected String password;

    @OneToMany
    private List<Bill> bills;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable
    protected Set<Role> roles = new HashSet<>();

    public Guest(String username, String password,Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public Guest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", bills=" + bills +
                ", roles=" + roles +
                '}';
    }
}
