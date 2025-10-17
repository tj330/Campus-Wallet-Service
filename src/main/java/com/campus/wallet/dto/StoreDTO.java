package com.campus.wallet.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreDTO {
    private Integer storeId;
    private String storeName;
    private String category;
}
