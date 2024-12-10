package com.flower.shop.cphpetalstudio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import javax.validation.constraints.Min;


@Data
@NoArgsConstructor
public class AddToCartRequest {

    @NonNull
    private Long bouquetId; // The ID of the bouquet to add

    @Min(1)
    private int quantity;   // The quantity to add
}
