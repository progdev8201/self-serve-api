package com.model.entity;

import com.model.enums.RequestType;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private OrderItem orderItem;
    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private RequestType requestType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", orderItem=" + orderItem +
                ", requestType=" + requestType +
                '}';
    }
}
