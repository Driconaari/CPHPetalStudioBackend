package com.flower.shop.cphpetalstudio.dto;

import com.flower.shop.cphpetalstudio.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItemDTO {
    private String bouquetName;
    private String description;
    private double price;
    private int quantity;
    private String imageUrl;

    // Constructor to map CartItem to CartItemDTO
    public CartItemDTO(CartItem cartItem) {
        this.bouquetName = cartItem.getBouquet().getName();
        this.description = cartItem.getBouquet().getDescription();
        this.price = cartItem.getBouquet().getPrice().doubleValue(); // Convert BigDecimal to double
        this.quantity = cartItem.getQuantity();
        this.imageUrl = cartItem.getBouquet().getImageUrl();
    }
}