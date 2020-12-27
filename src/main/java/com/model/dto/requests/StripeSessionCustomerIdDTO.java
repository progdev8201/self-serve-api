package com.model.dto.requests;

public class StripeSessionCustomerIdDTO {
    private String customerId;

    private String message ;

    public String getMessage() {
        return message;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
