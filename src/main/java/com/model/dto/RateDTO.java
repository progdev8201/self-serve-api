package com.model.dto;

public class RateDTO {
    private Long id;
    private double rate;
    private String raterUsername;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getRaterUsername() {
        return raterUsername;
    }

    public void setRaterUsername(String raterUsername) {
        this.raterUsername = raterUsername;
    }

    @Override
    public String toString() {
        return "RateDTO{" +
                "id=" + id +
                ", rate=" + rate +
                ", raterUsername='" + raterUsername + '\'' +
                '}';
    }
}
