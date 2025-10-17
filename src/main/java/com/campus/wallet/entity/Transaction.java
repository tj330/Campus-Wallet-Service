package com.campus.wallet.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long txnId;

    @ManyToOne
    @JoinColumn(name = "admissionNo")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "storeId")
    private Store store;

    @Enumerated(EnumType.STRING)
    private TxnType txnType;
    private Double amount;
    private LocalDateTime txnTime;

    public enum TxnType {
        DEPOSIT, PAYMENT, REFUND, WITHDRAW
    }
}
