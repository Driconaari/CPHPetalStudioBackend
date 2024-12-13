package com.flower.shop.cphpetalstudio.dto;


import javax.validation.constraints.Min;

public class UpdateCartItemRequest {


    @Min(1)
    private int quantity;

    public int getQuantity() {
        return quantity;
    }


    // Getters and Setters
}
