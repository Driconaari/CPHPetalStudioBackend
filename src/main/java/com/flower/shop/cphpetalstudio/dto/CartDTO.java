package com.flower.shop.cphpetalstudio.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {

    private Long cartId;
    private Long userId;
    private List<CartItemDTO> items;

}
