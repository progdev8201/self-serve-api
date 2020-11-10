package com.model.dto;

import com.model.entity.Owner;
import com.model.entity.StripeSubscriptionProducts;

import javax.persistence.*;
import java.io.Serializable;

public class SubscriptionEntityDTO implements Serializable {
    private Long id;

    private String subscriptionId;

    private String object;

    private Long created;

    private Long periodStart;

    private Long periodEnd;

    private String status;

    private OwnerDTO owner;

    private StripeSubscriptionProductsDTO stripeSubscriptionProducts;

    private String errorMessage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public StripeSubscriptionProductsDTO getStripeSubscriptionProducts() {
        return stripeSubscriptionProducts;
    }

    public void setStripeSubscriptionProducts(StripeSubscriptionProductsDTO stripeSubscriptionProducts) {
        this.stripeSubscriptionProducts = stripeSubscriptionProducts;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public Long getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(Long periodStart) {
        this.periodStart = periodStart;
    }

    public Long getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(Long periodEnd) {
        this.periodEnd = periodEnd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OwnerDTO getOwner() {
        return owner;
    }

    public void setOwner(OwnerDTO owner) {
        this.owner = owner;
    }
}
