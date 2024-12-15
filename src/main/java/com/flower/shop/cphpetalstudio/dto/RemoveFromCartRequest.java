package com.flower.shop.cphpetalstudio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RemoveFromCartRequest {
    private Long bouquetId; // The ID of the bouquet to remove
    private  long userId; // The ID of the user to remove the bouquet from

    public long getUserId() {
        return userId;

    }
}
