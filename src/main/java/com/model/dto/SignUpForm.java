package com.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignUpForm {
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
    private String role;
    @NotBlank
    private String telephone;

    @NotBlank
    private String adresse;


    public SignUpForm(@NotBlank @Size(min = 3, max = 50) String username, @NotBlank @Size(min = 6, max = 40) String password, String phone, String role) {
        this.username = username;
        this.password = password;
        this.telephone = phone;
        this.role = role;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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

    public String getRole() {
        return this.role;
    }


    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "SignUpForm{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }
}
