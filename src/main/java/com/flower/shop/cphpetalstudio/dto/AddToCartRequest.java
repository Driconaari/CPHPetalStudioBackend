package com.flower.shop.cphpetalstudio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddToCartRequest {
    private Long bouquetId; // The ID of the bouquet to add
    private int quantity;   // The quantity to add
}
