package com.flower.shop.cphpetalstudio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import javax.validation.constraints.Min;


@Data
@NoArgsConstructor
public class AddToCartRequest {

    private long userId;    // The ID of the user to add the bouquet to

    @NonNull
    private Long bouquetId; // The ID of the bouquet to add

    @Min(1)
    private int quantity;   // The quantity to add
}
