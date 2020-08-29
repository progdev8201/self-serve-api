package com.model.dto;

public class ClientDTO extends GuestDTO{
    private String telephone;


    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return "ClientDTO{" +
                "telephone='" + telephone + '\'' +
                '}';
    }
}
