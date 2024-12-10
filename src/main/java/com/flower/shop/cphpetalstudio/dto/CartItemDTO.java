package com.flower.shop.cphpetalstudio.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

    private Long productId;
    private String productName;
    private int quantity;
    private double price;

}
