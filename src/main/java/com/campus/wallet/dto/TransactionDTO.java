package com.campus.wallet.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDTO {
    private Long txnId;
    private String admissionNo;
    private Integer storeId;
    private String txnType;
    private Double amount;
    private LocalDateTime txnTime;
}
