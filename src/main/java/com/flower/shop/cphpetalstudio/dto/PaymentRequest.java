package com.flower.shop.cphpetalstudio.dto; // Adjust package if necessary


import com.flower.shop.cphpetalstudio.entity.User;

public class PaymentRequest {
    private Long bouquetId;
    private int quantity;
    private boolean subscription;
    private String paymentPlan; // WEEKLY, MONTHLY, YEARLY
    private User user; // Add this field for mapping user

    // Getters and Setters
    public Long getBouquetId() {
        return bouquetId;
    }

    public void setBouquetId(Long bouquetId) {
        this.bouquetId = bouquetId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isSubscription() {
        return subscription;
    }

    public void setSubscription(boolean subscription) {
        this.subscription = subscription;
    }

    public String getPaymentPlan() {
        return paymentPlan;
    }

    public void setPaymentPlan(String paymentPlan) {
        this.paymentPlan = paymentPlan;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

