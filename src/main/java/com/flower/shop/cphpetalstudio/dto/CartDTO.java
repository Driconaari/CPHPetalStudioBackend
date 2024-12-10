package com.flower.shop.cphpetalstudio.dto;

import java.util.List;

public class CartDTO {

    private Long cartId;
    private Long userId;
    private List<CartItemDTO> items;  // List of items in the cart

    public static class CartItemDTO {
        private Long productId;
        private String productName;
        private int quantity;
        private double price;

        // Getters and Setters
    }

    // Getters and Setters for CartDTO
}
