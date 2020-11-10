package com.model.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class SubscriptionEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subscriptionId;

    private String object;

    private Long created;

    private Long periodStart;

    private Long periodEnd;

    private String status;

    @ManyToOne
    private StripeSubscriptionProducts stripeSubscriptionProducts;

    @OneToOne
    private Owner owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StripeSubscriptionProducts getStripeSubscriptionProducts() {
        return stripeSubscriptionProducts;
    }

    public void setStripeSubscriptionProducts(StripeSubscriptionProducts stripeSubscriptionProducts) {
        this.stripeSubscriptionProducts = stripeSubscriptionProducts;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionid) {
        this.subscriptionId = subscriptionid;
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

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}
