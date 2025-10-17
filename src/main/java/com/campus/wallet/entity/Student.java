package com.campus.wallet.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {
    @Id
    private String admissionNo;
    private String name;
    private String department;
    private String email;
    private Double balance;
}
