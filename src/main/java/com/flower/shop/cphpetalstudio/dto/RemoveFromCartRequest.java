package com.flower.shop.cphpetalstudio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RemoveFromCartRequest {
    private Long bouquetId; // The ID of the bouquet to remove
}
