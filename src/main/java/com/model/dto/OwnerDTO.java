package com.model.dto;

import java.util.List;
import java.util.Set;

public class OwnerDTO extends GuestDTO{
    private List<RestaurantDTO> restaurants;

    private String stripeAccountId;

    private String stripeCustomerId;

    private Boolean isStripeEnable;

    public List<RestaurantDTO> getRestaurants() {
        return restaurants;
    }

    public OwnerDTO(Long id, String username, String password, List<BillDTO> bills, Set<RoleDTO> roles, List<RestaurantDTO> restaurants, String stripeAccountId, String stripeCustomerId, Boolean isStripeEnable) {
        super(id, username, password, bills, roles);
        this.restaurants = restaurants;
        this.stripeAccountId = stripeAccountId;
        this.stripeCustomerId = stripeCustomerId;
        this.isStripeEnable = isStripeEnable;
    }

    public OwnerDTO() {
    }

    public void setRestaurants(List<RestaurantDTO> restaurants) {
        this.restaurants = restaurants;
    }

    public Boolean getStripeEnable() {
        return isStripeEnable;
    }

    public void setStripeEnable(Boolean stripeEnable) {
        isStripeEnable = stripeEnable;
    }

    public String getStripeAccountId() {
        return stripeAccountId;
    }

    public void setStripeAccountId(String stripeAccountId) {
        this.stripeAccountId = stripeAccountId;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    @Override
    public String toString() {
        return "OwnerDTO{" +
                "restaurants=" + restaurants +
                ", stripeAccountId='" + stripeAccountId + '\'' +
                ", stripeCustomerId='" + stripeCustomerId + '\'' +
                ", isStripeEnable=" + isStripeEnable +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }
}
