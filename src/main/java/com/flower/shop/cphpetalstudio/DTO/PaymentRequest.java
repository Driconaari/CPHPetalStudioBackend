package com.flower.shop.cphpetalstudio.DTO;

public class PaymentRequest {
    private Long bouquetId;
    private int quantity;
    private boolean subscription;
    private String paymentPlan; // WEEKLY, MONTHLY, YEARLY (if subscription)

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
}

